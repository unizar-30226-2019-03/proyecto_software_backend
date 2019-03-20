#!/usr/bin/env bash

sudo apt update
sudo apt-get install ruby2.5
sudo apt-get install wget
cd /home/ubuntu
wget https://aws-codedeploy-eu-central-1.s3.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
rm install
mkdir unicast_backend
sudo apt-get install openjdk-8-jre
