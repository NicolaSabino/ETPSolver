# ETPSolver
## Optimization methods and algorithms class - PoliTO

A simple java CLI application that solve some given instances structured in such a way:

Each instance instanceXX is defined by 3 plain text files, with the same name:
* **InstanceXX.exm**: defines the total number of students enrolled per exam.
Format: 1 line per exam. Each line has the format
```
INT1 INT2
```
where INT1 is the exam ID and INT2 is the number of enrolled students in INT1.

* **instanceXX.slo**: defines the length of the examination period.
Format: a single value in the format
```
INT
```
where INT is the number of available time-slots t max .

* **instanceXX.stu**: defines the exams in which each student is enrolled.
Format: 1 line for each enrollment. Each line has the format
```
sINT INT2
```
where INT1 is the student ID and INT2 is the ID of the exam in which student
INT1 is enrolled.

Each solution provided by the program is a text file named **instancename_OMAXX groupYY.sol** containing, for
each exam, the assigned time-slot

The correct syntax for using the program is
```
java -jar ETPsolver_OMAAL_group24.jar [PATH FILE WITHOUT EXTENSION] -t [SECONDS] 
```
## Benchmarks

|            | Exams | Students | Enrolments | Timeslots | Density | Referenc | Benchmark | Gap % |
|------------|-------|----------|------------|-----------|---------|----------|-----------|-------|
| instance01 |   139 |      611 |       5751 |        13 |    0,14 |  157,033 |   158,430 |  0,89 |
| instance02 |   181 |      941 |       6034 |        21 |    0,29 |   34,709 |    40,808 | 17,57 |
| instance03 |   190 |     1125 |       8109 |        24 |    0,27 |   32,627 |    37,682 | 15,49 |
| instance04 |   261 |     4360 |      14901 |        23 |    0,18 |    7,717 |     9,290 | 20,38 |
| instance05 |   461 |     5349 |      25113 |        20 |    0,06 |   12,901 |    15,210 | 17,90 |
| instance06 |   622 |    21266 |      58979 |        35 |    0,13 |    3,045 |     3,790 | 24,48 |
| instance07 |    81 |     2823 |      10632 |        18 |    0,42 |   10,050 |    11,150 | 10,94 |




