#!/bin/sh

FORTRAN_REGEX=$1

if [ -z "$FORTRAN_REGEX" ]; then
    echo "Usage: $0 <fortran_file_regex>"
    echo "Example: $0 '.*\.f90$'"
    exit 1
fi

# Find all files matching the regex pattern
MATCHING_FILES=$(find . -type f -regex "$FORTRAN_REGEX")

if [ -z "$MATCHING_FILES" ]; then
    echo "Error: No files matching regex '$FORTRAN_REGEX' found."
    exit 1
fi

# Process each matching file
echo "$MATCHING_FILES" | while read -r FORTRAN_FILE; do
    FOLDER_PATH=$(dirname "$FORTRAN_FILE")
    BASE_NAME=$(basename "$FORTRAN_FILE")
    BASE_NAME="${BASE_NAME%.*}"
    DUMP_FILE="${FOLDER_PATH}/${BASE_NAME}.json"

    echo "Dumping AST for '$FORTRAN_FILE' to '$DUMP_FILE'..."

    flang-23 -fc1 -fopenmp -fintrinsic-modules-path "/tmp/metafor_$USER/modules" \
        -load "/tmp/metafor_$USER/DumpASTPlugin.so" -plugin dump-ast "$FORTRAN_FILE" > tmp.json || exit 1
    jq . tmp.json > "$DUMP_FILE"
    rm tmp.json
done
