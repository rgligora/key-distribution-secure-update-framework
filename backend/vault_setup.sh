#!/bin/sh

vault server -config=/vault/config/vault-config.hcl &
sleep 5

export VAULT_ADDR='http://127.0.0.1:8200'
export VAULT_API_ADDR=http://127.0.0.1:8200

vault operator init -key-shares=1 -key-threshold=1 -format=json > /vault/file/init-keys.json
VAULT_UNSEAL_KEY=$(jq -r ".unseal_keys_b64[0]" /vault/file/init-keys.json)
VAULT_ROOT_TOKEN=$(jq -r ".root_token" /vault/file/init-keys.json)
vault operator unseal $VAULT_UNSEAL_KEY


export VAULT_TOKEN=$VAULT_ROOT_TOKEN
vault token create -id=myroot -policy=root

echo "myroot" > /vault/file/myroot_token
export VAULT_TOKEN=myroot

curl --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{"type":"kv-v2"}' $VAULT_ADDR/v1/sys/mounts/secret
curl --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{"type":"transit"}' $VAULT_ADDR/v1/sys/mounts/transit


curl --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{
  "type": "ed25519"
}' $VAULT_ADDR/v1/transit/keys/software-signing-key


curl --header "X-Vault-Token: $VAULT_TOKEN" --request PUT --data '{
  "policy": "path \"secret/data/*\" {\n  capabilities = [\"create\", \"read\", \"update\", \"delete\", \"list\"]\n}\npath \"transit/sign/software-signing-key\" {\n  capabilities = [\"update\"]\n}\npath \"transit/verify/software-signing-key\" {\n  capabilities = [\"update\"]\n}\n"
}' $VAULT_ADDR/v1/sys/policies/acl/my-policy

curl --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{
  "policies": ["my-policy"],
  "id": "myroot"
}' $VAULT_ADDR/v1/auth/token/create

tail -f /dev/null
