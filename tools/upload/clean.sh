#!/bin/sh

echo "Now Cleaning up"
pwd
mkdir ./tmp
rm -f ./tmp/*
touch ./tmp/remove.me

echo "Finished"
