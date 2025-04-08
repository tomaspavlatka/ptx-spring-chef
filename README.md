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

## Payable leads mismatch

```sql
--- legacy way
select 
	loh.status historyStatus, loh."createdAt", 
	lo.id "offerId", lo.status offerStatus, lo.price,
	o.id "orgId", o."auth0Id"
from "LeadOfferHistory" loh 
join "LeadOffer" lo  on loh."leadOfferId"  = lo.id
join "Org" o on lo."orgId"  = o.id
where 
	loh."createdAt" between '2025-03-01 00:00:01' and '2025-03-31 23:59:59'
	and loh.status in ('ACCEPTED')
	and lo.price is not null
	and lo.price > 1
order by loh."createdAt" asc
```

Create 2 files, `bought-leads-{month}.csv` and `reclaimed-leads-{month}.csv`. Do not forget that we have 3 days grace period for reclaimed one.

