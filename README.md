# stfXCore

[![Build Status](https://travis-ci.com/EdgarACarneiro/stfXCore.svg?token=J52cxsfW92GANe4gUJgy&branch=master)](https://travis-ci.com/EdgarACarneiro/stfXCore)

The core microservice of the [___SpatioTemporal Feature eXtractor (stfX)___](https://github.com/EdgarACarneiro/stfX), contanining the Spring server that is presented to the end-user as an interface.

## Example requests

To provide stfX with a new storyboard one must call the `/storyboard` endpoint.

An example call to this endpoint is:
```json
{
  "dataset": [
    [
      [0 ,0],
      [0, 10],
      [10, 10],
      [10, 0]
    ],
    [
      [3.4, 5.1],
      [3.4, 15.1],
      [13.4, 15.1],
      [13.4, 5.1],
    ],
    ...
    [
      [100, 150],
      [100, 160],
      [110, 160],
      [110, 150]
    ]
  ]
}
```

And the respective example output (the storyboard ID):
```json
1
```

---

For getting the information of interest of a storyboard, one must call the `/storyboard/{id}` endpoint.

An example call to this is endpoint is:
```json
{
  "thresholds": {
    "translation": {
      "delta": [1, 1],
      "directedAcc": [5, 3],
      "absoluteAcc": [10, 10]
    },
    "rotation": {
      "delta": 5,
      "directedAcc": 30,
      "absoluteAcc": 45
    },
    "scale": {
      "delta":  0.05,
      "directedAcc": 0.3
    }
  }
}
```

And the respective output:
```json

```

The API definition is avaiable in [Swagger](https://app.swaggerhub.com/apis/EdgarACarneiro/thesis/1.0.0).


## Utils

This folder contains some useful scripts for the overall framework. A more detailed insight of each one is given below:

| Name | Functionality |
|:-|:-|
| __SPTDataLabParser__ | Parser of the format outputted by the _SPTDataLab_, to a valid json file. |
