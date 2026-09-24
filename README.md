# Order Processing Platform

Three Spring Boot 4.0.2 services running on Java 21, containerized with Docker, packaged with Helm, and deployable to Azure AKS through GitHub Actions.

## Services

| Service | Local port | Responsibility |
| --- | ---: | --- |
| `order-api` | 8080 | Validates and accepts orders |
| `order-processor` | 8081 | Processes accepted order events |
| `notification-service` | 8082 | Queues customer notifications |

The example implementation keeps business state in memory so it is easy to learn and run locally. A production extension would replace the in-memory boundary with a durable broker and database.

## Run and test locally

Prerequisites: Java 21 and Maven 3.9+.

```powershell
mvn test
mvn -pl services/order-api spring-boot:run
```

In separate terminals, run the other two modules with `mvn -pl services/order-processor spring-boot:run` and `mvn -pl services/notification-service spring-boot:run`.

```powershell
curl -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d '{"customerEmail":"buyer@example.com","product":"keyboard","quantity":1}'
curl http://localhost:8080/actuator/health
```

## Build containers

```powershell
docker build -f services/order-api/Dockerfile -t order-api:local .
docker build -f services/order-processor/Dockerfile -t order-processor:local .
docker build -f services/notification-service/Dockerfile -t notification-service:local .
```

## Infrastructure and AKS

The `infra/` folder is compatible with Terraform and OpenTofu. Copy `infra/terraform.tfvars.example` to `infra/terraform.tfvars`, set a real subscription ID locally, and follow [infra/README.md](infra/README.md). Do not commit credentials or state files.

## Helm deployment

```powershell
helm lint helm/order-platform
helm upgrade --install order-platform helm/order-platform `
  --set image.registry=<acr-login-server> `
  --set image.tag=<git-sha> --wait
```

The API is the only public `LoadBalancer`; processor and notification remain internal ClusterIP services. All pods run as non-root, expose actuator readiness/liveness probes, and use resource requests and limits.

## CI/CD setup

The workflow in `.github/workflows/ci-cd.yml` runs Maven verification and Gitleaks on pull requests. A push builds all three images, scans them with Trivy, and pushes the same `${{ github.sha }}` tag to ACR. A push to `main` deploys that exact tag with Helm and waits for the API rollout.

Configure these exact repository secrets under **Settings > Secrets and variables > Actions**:

| Secret | Value |
| --- | --- |
| `AZURE_CLIENT_ID` | Azure App Registration Application (client) ID |
| `AZURE_TENANT_ID` | Azure Entra Directory (tenant) ID |
| `AZURE_SUBSCRIPTION_ID` | Azure subscription ID from `terraform.tfvars` |
| `ACR_LOGIN_SERVER` | `orderplatformdevacr.azurecr.io` |
| `AKS_RESOURCE_GROUP` | `rg-order-platform-dev` |
| `AKS_CLUSTER_NAME` | `orderplatformdev-aks` |

Do not use the display name or object ID for `AZURE_CLIENT_ID`. The workflow generates `REGISTRY` from `ACR_LOGIN_SERVER` and `IMAGE_TAG` from `github.sha`; `GITHUB_TOKEN` is provided automatically by GitHub.

For local OpenTofu, the subscription can optionally be supplied as an environment variable:

```powershell
$env:TF_VAR_subscription_id = "<your-azure-subscription-id>"
```

Create an Azure federated credential for the service principal with issuer `https://token.actions.githubusercontent.com` and audience `api://AzureADTokenExchange`. Add a subject matching the workflow ref, for example `repo:Kuldeep-knoldus/order-processing-platform:ref:refs/heads/main`; add another credential for the feature branch if you want the image-push job to run there. Use an Azure federated identity for GitHub OIDC; never store a client secret in GitHub. The workflow now fails early with the missing secret names instead of the generic Azure login error.

## Documentation

- [Architecture and communication](docs/architecture.md)
- [Cost optimization](docs/cost-optimization.md)
- [Troubleshooting evidence](docs/troubleshooting.md)

## Demo answers

- Terraform/OpenTofu makes infrastructure repeatable, reviewed, and versioned.
- AKS provides scheduling, health management, rolling updates, and scaling for containers.
- CI validates and creates the artifact; CD promotes that immutable artifact to AKS.
- Cost is controlled with small node pools, right-sizing, autoscaling, budgets, and removal of idle resources.