# Pipeline

`Author: Marc Matija`

The Project employs 3 Different pipelines for various purposes.

## Deploy Pipeline

This Pipeline is run whenever a push to main is made it builds the Client and Server, deploys the client to Plesk,
by moving it's output to the `client-deploy` branch and in the future needs to build the docker container and deploy
them to a docker instance, which is yet to be done.

## Build Pipeline

This Pipeline is run whenever a push to a branch is made, that has an active pull request. It builds common, client and
server and runs the unit tests on them to see, if everything is still in order. In the final step, the javadoc 
documentation is built. All results are uploaded as pipeline artifacts, to be downloadable later.

## Documentation Pipeline

This Pipeline runs, whenever a push to dev is made and builds the writerside documentation and all javadoc documentation
and puts them on GitHub Pages to be visible on the web. This ensures the documentation is always up to date to what a
developer would find in the project.