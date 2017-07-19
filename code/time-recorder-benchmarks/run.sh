date 

# ~8 hours?
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 50000 -wbs 1 -i 500000 -bs 1 -f 3 -rf csv -rff sync-1-summary -o sync-1
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 5000 -wbs 10 -i 50000 -bs 10 -f 3 -rf csv -rff sync-10-summary -o sync-10
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 500 -wbs 100 -i 5000 -bs 100 -f 3 -rf csv -rff sync-100-summary -o sync-100
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 50 -wbs 1000 -i 500 -bs 1000 -f 3 -rf csv -rff sync-1000-summary -o sync-1000
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 5 -wbs 10000 -i 50 -bs 10000 -f 3 -rf csv -rff sync-10000-summary -o sync-10000
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 1 -wbs 100000 -i 5 -bs 100000 -f 3 -rf csv -rff sync-100000-summary -o sync-100000

java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 50000 -wbs 1 -i 500000 -bs 1 -f 3 -rf csv -rff async-1-summary -o async-1
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 5000 -wbs 10 -i 50000 -bs 10 -f 3 -rf csv -rff async-10-summary -o async-10
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 500 -wbs 100 -i 5000 -bs 100 -f 3 -rf csv -rff async-100-summary -o async-100
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 50 -wbs 1000 -i 500 -bs 1000 -f 3 -rf csv -rff async-1000-summary -o async-1000
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 5 -wbs 10000 -i 50 -bs 10000 -f 3 -rf csv -rff async-10000-summary -o async-10000
java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 1 -wbs 100000 -i 5 -bs 100000 -f 3 -rf csv -rff async-100000-summary -o async-100000


# 15 hours and 41 minutes (6 hours and 53 minutes + 8 hours and 48 minutes)
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 100000 -wbs 1 -i 1000000 -bs 1 -f 3 -rf csv -rff sync-1-summary -o sync-1
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 10000 -wbs 10 -i 100000 -bs 10 -f 3 -rf csv -rff sync-10-summary -o sync-10
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 1000 -wbs 100 -i 10000 -bs 100 -f 3 -rf csv -rff sync-100-summary -o sync-100
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 100 -wbs 1000 -i 1000 -bs 1000 -f 3 -rf csv -rff sync-1000-summary -o sync-1000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 10 -wbs 10000 -i 100 -bs 10000 -f 3 -rf csv -rff sync-10000-summary -o sync-10000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 1 -wbs 100000 -i 10 -bs 100000 -f 3 -rf csv -rff sync-100000-summary -o sync-100000

# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 100000 -wbs 1 -i 1000000 -bs 1 -f 3 -rf csv -rff async-1-summary -o async-1
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 10000 -wbs 10 -i 100000 -bs 10 -f 3 -rf csv -rff async-10-summary -o async-10
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 1000 -wbs 100 -i 10000 -bs 100 -f 3 -rf csv -rff async-100-summary -o async-100
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 100 -wbs 1000 -i 1000 -bs 1000 -f 3 -rf csv -rff async-1000-summary -o async-1000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 10 -wbs 10000 -i 100 -bs 10000 -f 3 -rf csv -rff async-10000-summary -o async-10000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 1 -wbs 100000 -i 10 -bs 100000 -f 3 -rf csv -rff async-100000-summary -o async-100000


# ~24 hours?
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 100000 -wbs 1 -i 1000000 -bs 1 -f 5 -rf csv -rff sync-1-summary -o sync-1
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 10000 -wbs 10 -i 100000 -bs 10 -f 5 -rf csv -rff sync-10-summary -o sync-10
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 1000 -wbs 100 -i 10000 -bs 100 -f 5 -rf csv -rff sync-100-summary -o sync-100
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 100 -wbs 1000 -i 1000 -bs 1000 -f 5 -rf csv -rff sync-1000-summary -o sync-1000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 10 -wbs 10000 -i 100 -bs 10000 -f 5 -rf csv -rff sync-10000-summary -o sync-10000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 1 -wbs 100000 -i 10 -bs 100000 -f 5 -rf csv -rff sync-100000-summary -o sync-100000

# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 100000 -wbs 1 -i 1000000 -bs 1 -f 5 -rf csv -rff async-1-summary -o async-1
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 10000 -wbs 10 -i 100000 -bs 10 -f 5 -rf csv -rff async-10-summary -o async-10
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 1000 -wbs 100 -i 10000 -bs 100 -f 5 -rf csv -rff async-100-summary -o async-100
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 100 -wbs 1000 -i 1000 -bs 1000 -f 5 -rf csv -rff async-1000-summary -o async-1000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 10 -wbs 10000 -i 100 -bs 10000 -f 5 -rf csv -rff async-10000-summary -o async-10000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 1 -wbs 100000 -i 10 -bs 100000 -f 5 -rf csv -rff async-100000-summary -o async-100000


# ~15 days?
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 1500000 -wbs 1 -i 15000000 -bs 1 -f 5 -rf csv -rff sync-1-summary -o sync-1
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 150000 -wbs 10 -i 1500000 -bs 10 -f 5 -rf csv -rff sync-10-summary -o sync-10
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 15000 -wbs 100 -i 150000 -bs 100 -f 5 -rf csv -rff sync-100-summary -o sync-100
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 1500 -wbs 1000 -i 15000 -bs 1000 -f 5 -rf csv -rff sync-1000-summary -o sync-1000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 150 -wbs 10000 -i 1500 -bs 10000 -f 5 -rf csv -rff sync-10000-summary -o sync-10000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.SyncInMemoryBenchmark.measure -wi 15 -wbs 100000 -i 150 -bs 100000 -f 5 -rf csv -rff sync-100000-summary -o sync-100000

# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 1500000 -wbs 1 -i 15000000 -bs 1 -f 5 -rf csv -rff async-1-summary -o async-1
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 150000 -wbs 10 -i 1500000 -bs 10 -f 5 -rf csv -rff async-10-summary -o async-10
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 15000 -wbs 100 -i 150000 -bs 100 -f 5 -rf csv -rff async-100-summary -o async-100
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 1500 -wbs 1000 -i 15000 -bs 1000 -f 5 -rf csv -rff async-1000-summary -o async-1000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 150 -wbs 10000 -i 1500 -bs 10000 -f 5 -rf csv -rff async-10000-summary -o async-10000
# java -Xms2048m -Xmx4096m -jar target/benchmarks.jar tech.dario.timecomplexityanalysis.timerecorder.benchmark.AsyncInMemoryBenchmark.measure -wi 15 -wbs 100000 -i 150 -bs 100000 -f 5 -rf csv -rff async-100000-summary -o async-100000

cat sync-1-summary sync-10-summary sync-100-summary sync-1000-summary sync-10000-summary sync-100000-summary async-1-summary async-10-summary async-100-summary async-1000-summary async-10000-summary async-100000-summary > all
