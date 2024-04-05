# Exercise 6.3
## Quality Gate

| Metric                     | Operator        | Value |
|----------------------------|-----------------|-------|
| Issues                     | is greater than | 0     |
| Security Hotspots Reviewed | is less than    | 100%  |
| Coverage                   | is less than    | 0.0%  |
| Duplicated Lines (%)       | is greater than | 0.1%  |

Coverage was set to 0% since there are no tests, so the quality gate would never pass.

Regarding duplications, there are none in our code, so any value can be accepted. I set it to 0.1% so it was more easy to break the quality gate.