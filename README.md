# Intro

A simple tool to report java process heap, memory and cpu usage inspired by [jps_stat](https://github.com/amarjeetanandsingh/jps_stat).

This only supports Ubuntu at the moment, if you wish to add support to your OS please submit a PR.

# Usage

```bash
./jps-stats

pid    name     heap   ram     cpu
------------------------------------
866    re-gent  78 MB  227 MB  .3
30484  core     31 MB  234 MB  1.6

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

