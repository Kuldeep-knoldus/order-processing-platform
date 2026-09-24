{{- define "order-platform.labels" -}}
app.kubernetes.io/name: {{ .name }}
app.kubernetes.io/part-of: order-platform
app.kubernetes.io/managed-by: Helm
{{- end }}