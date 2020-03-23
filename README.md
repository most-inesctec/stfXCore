# stfXCore

[![BCH compliance](https://bettercodehub.com/edge/badge/EdgarACarneiro/stfXCore?branch=master&token=351fd328face954f0ca7cfabd6e380c4dd4b4810)](https://bettercodehub.com/)
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

And a possible respective output:
```json
[
  {
    "trigger": "DIRECTED_ACC",
    "type": "TRANSLATION",
    "triggerValue": 3.399834,
    "phenomena": [
      {
        "representation": [
          [
            0,
            0
          ],
          [
            2,
            0
          ],
          [
            2,
            2
          ],
          [
            1,
            2
          ],
          [
            0,
            2
          ],
          [
            0,
            0
          ]
        ],
        "timestamp": 0
      },
      {
        "representation": [
          [
            0.49908704,
            0.50115657
          ],
          [
            2.522014,
            0.50044763
          ],
          [
            2.524941,
            2.5197387
          ],
          [
            1.5078377,
            2.5364616
          ],
          [
            0.49437168,
            2.5216413
          ],
          [
            0.49908704,
            0.50115657
          ]
        ],
        "timestamp": 3
      },
      {
        "representation": [
          [
            0.9981914,
            1.0022594
          ],
          [
            3.044001,
            1.000869
          ],
          [
            3.049811,
            3.0394788
          ],
          [
            2.0157335,
            3.0730088
          ],
          [
            0.98884827,
            3.0432403
          ],
          [
            0.9981914,
            1.0022594
          ]
        ],
        "timestamp": 6
      },
      {
        "representation": [
          [
            1.4973129,
            1.5033095
          ],
          [
            3.565961,
            1.5012648
          ],
          [
            3.574609,
            3.5592203
          ],
          [
            2.5236866,
            3.6096408
          ],
          [
            1.4834298,
            3.5647972
          ],
          [
            1.4973129,
            1.5033095
          ]
        ],
        "timestamp": 9
      },
      {
        "representation": [
          [
            1.9964514,
            2.0043077
          ],
          [
            4.0878925,
            2.0016356
          ],
          [
            4.099334,
            4.0789633
          ],
          [
            3.0316966,
            4.146358
          ],
          [
            1.9781165,
            4.0863123
          ],
          [
            1.9964514,
            2.0043077
          ]
        ],
        "timestamp": 12
      },
      {
        "representation": [
          [
            2.4956067,
            2.505255
          ],
          [
            4.6097956,
            2.5019817
          ],
          [
            4.6239843,
            4.5987086
          ],
          [
            3.5397625,
            4.6831594
          ],
          [
            2.4729083,
            4.607786
          ],
          [
            2.4956067,
            2.505255
          ]
        ],
        "timestamp": 15
      }
    ]
  },
  ...
]
```

The API definition is avaiable in [Swagger](https://app.swaggerhub.com/apis/EdgarACarneiro/thesis/1.0.0).


## Utils

This folder contains some useful scripts for the overall framework. A more detailed insight of each one is given below:
|  Name | Functionality |
|:-|:-|
| __SPTDataLabParser__ | Parser of the format outputted by the _SPTDataLab_, to a valid json file. |
