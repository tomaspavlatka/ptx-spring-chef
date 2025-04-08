# Tomas Pavlatka Personal Spring Chef

The collection of useful command for the everyday work.

## Companies incorrect

Checks the correctness of relationships between company and easibill customer. 

You have to export data from current DynamoDB table and move it under `src/main/resources/data`.

Due to very low easybill API limits, we have to split the exported file into many small one, ideally with 10 records per file. You can use the following script to do this.

```shell
tail -n +2 file.txt | split -l 4 - split_
for file in split_*
do
    head -n 1 file.txt > tmp_file
    cat "$file" >> tmp_file
    mv -f tmp_file "$file"
done
```
[Resource](https://stackoverflow.com/questions/1411713/how-to-split-a-file-and-keep-the-first-line-in-each-of-the-pieces)

