# Intro

A simple tool to report java processes:

* Heap usage
* RAM usage
* CPU usage
* GC usage

Inspired by [jps_stat](https://github.com/amarjeetanandsingh/jps_stat)

This only supports Ubuntu at the moment, if you wish to add support to your OS please submit a PR.

# Usage

```bash
$ jps-stats top

pid    name  heap   gc    ram    cpu
-------------------------------------
20699  main  311MB  0.0%  293MB  0.2%
21084  main  131MB  0.0%  964MB  1.8%


# Show raw data (no filtering)
$ jps-stats raw

{:pid 22873, :name Jps, :heap nil, :gc nil, :ram nil, :cpu nil}
{:pid 20699, :name main, :heap 311.74189453125, :gc 0.000M, :ram 293.8486328125, :cpu 0.2M}
{:pid 21084, :name main, :heap 149.922265625, :gc 0.000M, :ram 964.1796875, :cpu 1.8M}

# Filter pid's with partial data same as top
$ jps_stat raw --f true
```

# Build

Make sure to have the latest graalvm native-image tool:

```bash
$ lein uberjar
$ native-image -jar target/jps-stat-0.2.2-standalone.jar --no-fallback --report-unsupported-elements-at-runtime --initialize-at-build-time --allow-incomplete-classpath
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

