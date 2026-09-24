# Troubleshooting evidence

## Deliberate deployment fault

The initial deployment experiment used an image tag that did not exist in ACR. The deployment accepted the manifest, but pods stayed in `ImagePullBackOff`.

## Diagnosis

```powershell
kubectl get pods
kubectl describe pod <pod-name>
kubectl get events --sort-by=.lastTimestamp
```

The pod events reported that the registry could not find the requested tag. This distinguished an image publication problem from a readiness probe failure.

## Fix and prevention

The workflow now sets `IMAGE_TAG=${{ github.sha }}` once, builds and scans each image with that value, pushes it, and passes the same value to Helm. The deployment waits for rollout completion. Verify the fix with:

```powershell
kubectl get deployment order-api -o jsonpath='{.spec.template.spec.containers[0].image}'
kubectl rollout status deployment/order-api --timeout=120s
```

The image digest and commit SHA can then be matched in the ACR and GitHub Actions logs.