#!/bin/bash

find . -type f -name "*.tex" | while read file; do echo $file; ./latexindent.pl -w -s -l "$file";done
