# stfXCore

[![BCH compliance](https://bettercodehub.com/edge/badge/EdgarACarneiro/stfXCore?branch=master&token=351fd328face954f0ca7cfabd6e380c4dd4b4810)](https://bettercodehub.com/)
[![Build Status](https://travis-ci.com/EdgarACarneiro/stfXCore.svg?token=J52cxsfW92GANe4gUJgy&branch=master)](https://travis-ci.com/EdgarACarneiro/stfXCore)

The core microservice of the [___SpatioTemporal Feature eXtractor (stfX)___](https://github.com/EdgarACarneiro/stfX), contanining the Spring server that is presented to the end-user as an interface.

This server's updated API can be consulte [here](https://app.swaggerhub.com/apis-docs/EdgarACarneiro/thesis/2.0.0#/).

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
  ],
  "metadata": {
    "timePeriod": 3
  }
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
  "parameters": {
    "translation": {
      "delta": 1,
      "directedAcc": 5,
      "absoluteAcc": 10,
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

And a possible respective output:
```json
[
  {
    "events": [
      {
        "trigger": "DIRECTED_ACC",
        "type": "ROTATION",
        "triggerValue": 0.061550736
      },
      {
        "trigger": "DIRECTED_ACC",
        "type": "TRANSLATION",
        "triggerValue": 3.08966
      },
      {
        "trigger": "DIRECTED_ACC",
        "type": "UNIFORM_SCALE",
        "triggerValue": -0.046621263
      }
    ],
    "temporalRange": [
      84,
      99
    ],
    "phenomena": [
      {
        "representation": [
          [
            14.080577,
            14.11513
          ],
          [
            16.708536,
            14.104435
          ],
          [
            16.772493,
            16.65774
          ],
          [
            15.341294,
            17.15984
          ],
          [
            13.982059,
            16.69499
          ],
          [
            14.080577,
            14.11513
          ]
        ],
        "timestamp": 84
      },
      {
        "representation": [
          [
            14.580127,
            14.615127
          ],
          [
            17.229578,
            14.604353
          ],
          [
            17.29503,
            17.17758
          ],
          [
            15.850552,
            17.698574
          ],
          [
            14.47946,
            17.215542
          ],
          [
            14.580127,
            14.615127
          ]
        ],
        "timestamp": 87
      },
      {
        "representation": [
          [
            15.12965,
            15.165091
          ],
          [
            17.80268,
            15.15425
          ],
          [
            17.869707,
            17.749409
          ],
          [
            16.410786,
            18.291265
          ],
          [
            15.026731,
            17.78811
          ],
          [
            15.12965,
            15.165091
          ]
        ],
        "timestamp": 90
      },
      {
        "representation": [
          [
            15.629235,
            15.66503
          ],
          [
            18.323635,
            15.654145
          ],
          [
            18.392036,
            18.26926
          ],
          [
            16.920135,
            18.830154
          ],
          [
            15.524367,
            18.30859
          ],
          [
            15.629235,
            15.66503
          ]
        ],
        "timestamp": 93
      },
      {
        "representation": [
          [
            16.128838,
            16.164942
          ],
          [
            18.844551,
            16.15403
          ],
          [
            18.914263,
            18.789116
          ],
          [
            17.429527,
            19.369116
          ],
          [
            16.022116,
            18.829037
          ],
          [
            16.128838,
            16.164942
          ]
        ],
        "timestamp": 96
      },
      {
        "representation": [
          [
            16.628456,
            16.664831
          ],
          [
            19.365423,
            16.653904
          ],
          [
            19.43639,
            19.308979
          ],
          [
            17.938961,
            19.90815
          ],
          [
            16.519976,
            19.34945
          ],
          [
            16.628456,
            16.664831
          ]
        ],
        "timestamp": 99
      }
    ]
  },
  ...
]
```

The API definition is avaiable in [Swagger](https://app.swaggerhub.com/apis/EdgarACarneiro/thesis/1.0.0).

## Dockerization

To run a container with the application, in a standalone manner, use:
```shell
sh docker-build.sh
docker run -p 8080:8080 stfx-core
```

## Utils

This folder contains some useful scripts for the overall framework. A more detailed insight of each one is given below:
|  Name | Functionality |
|:-|:-|
| __SPTDataLabParser__ | Parser of the format outputted by the _SPTDataLab_, to a valid json file. |
