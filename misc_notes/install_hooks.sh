#!/usr/bin/env bash

set -e

for repo in */; do
    if [ -d "$repo/.git" ] && [ -d "$repo/pre-commit-hooks" ]; then
        echo "Installing hooks for $repo"

        cp -rv "$repo/pre-commit-hooks/." "$repo/.git/hooks/"

        find "$repo/.git/hooks" -type f \
            -name 'pre-commit*' \
            -exec chmod +x {} \;
    fi
done
