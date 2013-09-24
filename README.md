This is a Sample Java Application for OpenShift Tomcat cartridges, that is used to demonstrate some of the 
OpenShift basics. Primarly how Datasource work. 

This uses the OpenShift `jbossews` cartridge documentation can be found at:

https://github.com/openshift/origin-server/tree/master/cartridges/openshift-origin-cartridge-jbossews/README.md

To use this set of examles create a Java Application following the 
[online examples](https://www.openshift.com/get-started/) or with:

```
rhc create-app -a APP_NAME -t jbossews-2.0
```
Following this you can pull in the code from these examples by using:

```
git remote add examples https://github.com/gssOpenShiftsupportExamples/Tomcat_Example.git
git fetch examples
git checkout master; git merge --strategy=recursive -X theirs examples/master
git push
```
- **Note:** Then change in the merge strategy is so you can avoid conflicts. 

You can then navigate to **http://APP_NAME-NAMESPACE.rhcloud.com/** to see this samle in action.

This example will requre that you also install either the `mysql-5.1` or the `postgresql-8.4` cartridge.
These can be added to your application using one of the following commands. 

```
rhc cartridge add postgresql-8.4 -a APP_NAME

rhc cartridge add mysql-5.1 -a APP_NAME
```
