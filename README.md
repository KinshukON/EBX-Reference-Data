# EBX Reference Data

Reference-data management module for ON EBX FastTrack.

## Modules

- `referencedata-lib`: reusable Java services and components.
- `referencedata-web`: EBX module packaging, schemas, web resources, and reference datasets.

## Build

The build requires Java 17 or later and an EBX server library directory containing `ebx-lib.jar`.

```bash
export EBX_LIB_DIR=/path/to/ebx-server/lib
mvn clean package
```

The project can also be included as the `EBX-Reference-Data` module in the ON EBX FastTrack Maven reactor.
