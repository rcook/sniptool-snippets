#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

this_dir=$(cd $(dirname $0); pwd -P)
echo $this_dir