---
---

# Federation Registry

## Server to edit

The HKAF federation registry is currently deployed on the server `afed2.hkaf.edu.hk`

### Software

Install the following software

    // As root
    yum install git
    yum install java-1.7.0-openjdk-devel
    yum install unzip
    
    // As a standard user
    curl -s "https://get.sdkman.io" | bash
    // You must logout and login
    sdk install grails 2.2.4
    sdk install groovy


## Get the repository

    git clone https://github.com/hkafaccessfed/federationregistry.git

## Use the correct branch

    cd federationregistry
    git branch hk/branding
    git checkout hk/branding

## Change the email templates

The workflow email templates can be found here:
 
    cd federationregistry/branding/grails-app/views/templates/mail/workflows/default

| File name | Description |
|-----------|-------------|
| _activated_idp.gsp | User is informed that their IdP has been approved and provided guidance on how to complete to registration |
| _registered_idp.gsp | Confirmation that IdP registration has been completed successfully but approval is still to occur |
| _rejected_idp.gsp | User is informed that their IdP registration has been rejected |
| _activated_organization.gsp | User is informed that their Organisation has been approved |
| _registered_organization.gsp | Confirmation that Organisation registration has been completed successfully but approval is still to occur |
| _rejected_organization.gsp | User is informed that their Organisation registration has been rejected |
| _activated_sp.gsp | User is informed that their SP has been approved and provided guidance on how to complete to registration |
| _registered_sp.gsp | Confirmation that SP registration has been completed successfully but approval is still to occur |
| _rejected_sp.gsp | User is informed that their SP registration has been rejected |

## Import the themes

After you make your changes to the email templates you must import them into the application prior to compiling and deploying

    cd federationregistry/app/federationregistry

	grails importtheme

## Building a new .war file

To create a new .war file for the federation registry execute the following commands

    cd federationregistry/app/federationregistry

    export fr_config=/opt/federationregistry/application/config/fr-config.groovy

    grails complie

    grails war

If successful the new .war file will appear in `federationregistry/app/federationregistry/target`

## Deploy .war file

Ensure you keep an copy of the old .war file just in case you need to rollback to the previous versions.

The .war file is deployed on the FR server in the directory `/opt/federationregistry/application/war`. Copy the .war file create above to this location and ensure the symbolic link from `current` point the correct version.

## Restarting the Federation Registry

To restart FR use the following command

    systemctl restart federationregistry

## Saving changes

Save your changes to github. Commit and push your changes to github. 

Note: You should have a source code development work flow in place to ensure all changes are checked before being merged into your branch.  
