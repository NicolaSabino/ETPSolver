# ETPSolver
**Antonella Cardillo, Maria Castaldo, Marian Alexandru Ilies, Fabio Gavinelli, Nicola Sabino**  


*Optimization methods and algorithms class - PoliTO*

*See the [Report](https://github.com/NicolaSabino/ETPSolver/blob/master/Report.pdf) file for more details about the implementation.*

A simple java CLI application that solve some given instances structured in such a way

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
|            | Exams | Students | Enrolments | Timeslots | Density | Reference | Benchmark | Gap % |
|------------|-------|----------|------------|-----------|---------|-----------|-----------|-------|
| instance01 |   139 |      611 |       5751 |        13 |    0,14 |   157,033 |   157,317 |  0,18 |
| instance02 |   181 |      941 |       6034 |        21 |    0,29 |    34,709 |    36,577 |  5,38 |
| instance03 |   190 |     1125 |       8109 |        24 |    0,27 |    32,627 |    33,886 |  3,86 |
| instance04 |   261 |     4360 |      14901 |        23 |    0,18 |     7,717 |     8,407 |  8,94 |
| instance05 |   461 |     5349 |      25113 |        20 |    0,06 |    12,901 |    14,246 | 10,42 |
| instance06 |   622 |    21266 |      58979 |        35 |    0,13 |     3,045 |     3,747 | 23,07 |
| instance07 |    81 |     2823 |      10632 |        18 |    0,42 |    10,050 |    10,498 |  4,45 |

|        |   %   |
|--------|-------|
| Best:  |  0,18 |
| Avg:   |  8,04 |
| Worst: | 23,07 |




