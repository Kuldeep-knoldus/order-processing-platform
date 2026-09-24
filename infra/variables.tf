variable "subscription_id" { type = string; sensitive = true }
variable "location" { type = string; default = "Central India" }
variable "resource_group_name" { type = string; default = "rg-order-platform" }
variable "name_prefix" { type = string; default = "orderplatform" }
variable "kubernetes_version" { type = string; default = null }
variable "node_count" { type = number; default = 1 }
variable "node_vm_size" { type = string; default = "Standard_B2s" }