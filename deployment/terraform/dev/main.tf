locals {
  context = {
    project_name = "buchsbaum"
    environment_name = "dev"
    key_name = "btax"
  }
}

provider "aws" {
  region = "eu-central-1"
  shared_credentials_file = "/Users/shuie/dev/certificates/aws-credentials"
  profile = "sifra"
}

module "network" {
  source = "github.com/SifraDigital/DevOpsCommon//terraform/modules/network"
  context = local.context

  domain_name = "btax.sifradigital.dev"
  root_domain = "sifradigital.dev."
}

module "app" {
  source = "github.com/SifraDigital/DevOpsCommon//terraform/modules/app"
  context = local.context

  network = module.network
  ubuntu_ami = "ami-0b063c60b220a0574" // Ubuntu 20.04
}

module "web" {
  source = "github.com/SifraDigital/DevOpsCommon//terraform/modules/web"
  context = local.context

  network = module.network
  ubuntu_ami = "ami-0b063c60b220a0574" // Ubuntu 20.04
}


module "database" {
  source = "github.com/SifraDigital/DevOpsCommon//terraform/modules/database"
  context = local.context

  postgres_version = "12.7"
}

resource "local_file" "ansible" {
  content = templatefile("../ansible.tpl", {
    app_ip = module.app.app_ip
    web_ip = module.web.web_ip
    database_host = module.database.database_host
    database_name = module.database.database_name
    database_username = module.database.database_username
    key_name = local.context.key_name
    project_name = local.context.project_name
  })
  filename = "../../ansible/env/dev"
}

resource "local_file" "iterm" {
  content = templatefile("../iterm.json", {
    app_ip = module.app.app_ip
    web_ip = module.web.web_ip
    key_name = local.context.key_name
    project_name = local.context.project_name
    environment_name = local.context.environment_name
  })
  filename = "/Users/shuie/Library/Application Support/iTerm2/DynamicProfiles/${local.context.project_name}-${local.context.environment_name}.json"
}