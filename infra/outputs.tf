output "resource_group_name" { value = azurerm_resource_group.platform.name }
output "acr_login_server" { value = azurerm_container_registry.platform.login_server }
output "aks_name" { value = azurerm_kubernetes_cluster.platform.name }