# Quarkus MVC sample app
A few things are different from the preferred Krazo setup:

1. Your views etc. shouldn't reside in `src/main/webapp`, but in `src/main/resources/META-INF/resources`.
This is the place where Quarkus expects static resources to reside.
For example, `index.html` should live in `src/main/resources/META-INF/resources/index.html`.