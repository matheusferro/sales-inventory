# Sales inventory
Implementation of [solution](https://medium.com/deutsche-telekom-gurgaon/how-to-design-sales-inventory-microservice-based-on-event-driven-architecture-e6f67e3a2e91).

### Utils
``` Shell
curl -XPUT -H "Content-Type: application/json" \ 
    https://[YOUR_ELASTICSEARCH_ENDPOINT]:9200/_all/_settings \ 
    -d '{"index.blocks.read_only_allow_delete": null}' \
```

``` Shell
rs.initiate({
_id : 'rs0',
members: [
        { _id : 0, host: "mongodb:27017" },
        { _id : 1, host: "mongodb2:27017" }
    ]
})
```