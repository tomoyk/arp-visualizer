#!/bin/bash -xu

curl http://standards-oui.ieee.org/oui/oui.txt > vendorList

cat vendorList | grep ^[0-9a-zA-Z] | grep hex | sort | cut -f1,3 | sed 's/  (hex)//g' > vendorList2

exit 0
