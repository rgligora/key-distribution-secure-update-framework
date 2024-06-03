#!/bin/sh

vault server -config=/vault/config/vault-config.hcl &
sleep 5

export VAULT_ADDR='http://127.0.0.1:8200'
export VAULT_API_ADDR='http://127.0.0.1:8200'


vault_init=$(curl --silent --request POST --data '{"secret_shares":1, "secret_threshold":1}' $VAULT_ADDR/v1/sys/init)
VAULT_UNSEAL_KEY=$(echo $vault_init | jq -r ".keys_base64[0]")
VAULT_ROOT_TOKEN=$(echo $vault_init | jq -r ".root_token")


curl --silent --request POST --data '{"key":"'"$VAULT_UNSEAL_KEY"'"}' $VAULT_ADDR/v1/sys/unseal


export VAULT_TOKEN=$VAULT_ROOT_TOKEN
echo "myroot" > /vault/file/myroot_token


curl --silent --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{"type":"kv-v2"}' $VAULT_ADDR/v1/sys/mounts/secret

curl --silent --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{"type":"transit"}' $VAULT_ADDR/v1/sys/mounts/transit

curl --silent --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{"type": "rsa-2048"}' $VAULT_ADDR/v1/transit/keys/backend

curl --silent --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{"type": "rsa-2048"}' $VAULT_ADDR/v1/transit/keys/software-signing-key

curl --silent --header "X-Vault-Token: $VAULT_TOKEN" --request PUT --data '{
  "policy": "path \"secret/data/*\" {\n  capabilities = [\"create\", \"read\", \"update\", \"delete\", \"list\"]\n}\npath \"transit/keys/*\" {\n  capabilities = [\"create\", \"read\", \"update\"]\n}\npath \"transit/sign/*\" {\n  capabilities = [\"create\", \"read\", \"update\"]\n}\npath \"transit/verify/*\" {\n  capabilities = [\"create\", \"read\", \"update\"]\n}\n"
}' $VAULT_ADDR/v1/sys/policies/acl/my-policy

curl --silent --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{
  "policies": ["my-policy"],
  "id": "myroot"
}' $VAULT_ADDR/v1/auth/token/create

tail -f /dev/null
