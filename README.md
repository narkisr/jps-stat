# Intro

A simple tool to report java process heap, memory and cpu usage inspired by [jps_stat](https://github.com/amarjeetanandsingh/jps_stat).

This only supports Ubuntu at the moment, if you wish to add support to your OS please submit a PR.

# Usage

```bash
$ jps-stats top

pid    name     heap   ram     cpu
------------------------------------
866    re-gent  78 MB  227MB  .3
30484  core     31 MB  234MB  1.6


# Show raw data (no filtering)
$ jps-stats raw

{:pid 906, :name main, :heap 490.65302734375, :ram 963.4013671875, :cpu nil}
{:pid 164, :name Jps, :heap nil, :ram nil, :cpu nil}
{:pid 169, :name main, :heap 31.635546875, :ram 231.15234375, :cpu 239M}
{:pid 138, :name main, :heap 22.60009765625, :ram 173.7705078125, :cpu nil}
{:pid 2849, :name main, :heap 293.2935546875, :ram 294.8759765625, :cpu 3.7M}

# Filter pid's with partial data same as top
$ jps_stat raw --f true
```

# Build

Make sure to have the latest graalvm native-image tool:

```bash
$ lein uberjar
$ native-image -jar target/jps-stat-0.1.0-standalone.jar --no-fallback --report-unsupported-elements-at-runtime --initialize-at-build-time --allow-incomplete-classpath
```

# Copyright and license

Copyright [2020] [Ronen Narkis]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

