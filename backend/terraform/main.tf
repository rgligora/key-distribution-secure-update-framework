provider "vault" {
  address = "http://127.0.0.1:8200"
}

resource "vault_auth_backend" "approle" {
  type = "approle"
}

resource "vault_secret_backend" "pki" {
  path = "pki"
  type = "pki"
  description = "PKI backend for issuing certificates"
}

resource "vault_mount" "pki_tune" {
  path = "pki"
  type = "pki"
  description = "PKI backend for issuing certificates"
  default_lease_ttl_seconds = 31536000  # 1 year
  max_lease_ttl_seconds     = 31536000  # 1 year
}

resource "vault_pki_secret_backend_root" "generate" {
  backend      = vault_secret_backend.pki.path
  common_name  = "example.com"
  ttl          = "8760h"
}

resource "vault_pki_secret_backend_role" "client_cert" {
  backend       = vault_secret_backend.pki.path
  name          = "client-cert"
  allowed_domains = "example.com"
  allow_subdomains = true
  max_ttl       = "72h"
}

resource "vault_pki_secret_backend_config_urls" "urls" {
  backend = vault_secret_backend.pki.path

  issuing_certificates    = "http://127.0.0.1:8200/v1/pki/ca"
  crl_distribution_points = "http://127.0.0.1:8200/v1/pki/crl"
}
