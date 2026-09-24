# Azure cost review

Review actual usage in Cost Management after deployment and set a budget alert before increasing capacity. Practical actions for this platform:

1. Use a small B-series node pool for development and schedule the dev resource group to scale to zero or be deallocated outside working hours.
2. Start with one node and one replica, then right-size CPU and memory from AKS metrics instead of reserving capacity speculatively.
3. Use Horizontal Pod Autoscaler and cluster autoscaler when traffic justifies them; set maximums so a traffic spike cannot create uncontrolled spend.
4. Use ACR Basic for learning, enable retention policies, and remove unreferenced SHA images after a defined retention period.
5. Use Azure Advisor and Cost Management budgets with alerts at 50%, 80%, and 100%; review idle public IPs, load balancers, disks, and resource groups weekly.

Production tradeoff: cost reduction must not remove availability or observability requirements. Validate any scale-down against SLOs and deployment windows.