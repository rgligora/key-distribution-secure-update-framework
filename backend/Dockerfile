FROM hashicorp/vault:1.9.0

ENV TERRAFORM_VERSION=1.3.6

RUN apk update && apk add --no-cache \
    unzip \
    curl \
    jq && \
    curl -LO https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip && \
    unzip terraform_${TERRAFORM_VERSION}_linux_amd64.zip && \
    mv terraform /usr/local/bin/ && \
    rm terraform_${TERRAFORM_VERSION}_linux_amd64.zip

COPY vault/vault-config.hcl /vault/config/
COPY vault_setup.sh /usr/local/bin/

RUN chmod +x /usr/local/bin/vault_setup.sh

ENTRYPOINT ["/usr/local/bin/vault_setup.sh"]

