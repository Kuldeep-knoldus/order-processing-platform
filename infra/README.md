# Infrastructure

This OpenTofu/Terraform-compatible configuration creates an Azure resource group, Basic ACR, and a small AKS cluster. It uses managed identity and grants the cluster kubelet `AcrPull`; no registry passwords are required.

```powershell
az login
tofu init # or terraform init
tofu plan -var-file=terraform.tfvars
tofu apply -var-file=terraform.tfvars
az aks get-credentials --resource-group rg-order-platform-dev --name orderplatformdev-aks
```

Keep `terraform.tfvars` local. It is ignored by Git and must never contain credentials committed to the repository.