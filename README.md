# Continuous OpenAPI Security Tests on K8s with Testkube and ZAP

Demo repository for the DevSecCon22 Lightning Talk on Continuous OpenAPI Security Tests on K8s with Testkube and ZAP.

## Bootstrapping

For a simple local setup (e.g. on Rancher Desktop) use the following instructions:

```bash
# the ZAP setup
kubectl apply -f k8s/zap/zap-namespace.yaml 
kubectl apply -f k8s/zap/
kubectl get all -n zap

# the TestKube setup
kubectl testkube init
kubectl get ns
kubectl get all -n testkube
kubectl testkube dashboard
```

For a GKE based setup using Flux as GitOps tool, use the following instructions:

```bash
# define required ENV variables for the next steps to work
export GITHUB_USER=lreimer
export GITHUB_TOKEN=<your-token>

# setup a GKE cluster with Flux2
make create-gke-cluster
make bootstrap-gke-flux2
make delete-gke-cluster
```

## Usage

```bash
# deploying a demo microservice
kubectl apply -f k8s/application/
kubectl get all

# you can use ZAP via the UI in a browser
kubectl port-forward pod/zap-webswing -n zap 8080:8080
open http://localhost:8080/zap

# you can use ZAP via its API programmatically
kubectl port-forward pod/zap-daemon -n zap 9080:9080
./gradlew test

# create a Gradle test for this Git repo
kubectl testkube create test --git-uri https://github.com/lreimer/testkube-zap-demo.git --git-branch main --type "gradle/test" --name gradle-test
kubectl testkube run test --watch gradle-test

# create a ZAP test with inline content
kubectl testkube create test --file src/test/zap/zap-api-test.yaml --type "zap/api" --name zap-api-test
kubectl testkube run test --watch zap-api-test

kubectl testkube create test --file src/test/zap/zap-api-test.yaml --type "zap/api" --name zap-api-scheduled-test

# create a ZAP test for this Git repo
kubectl testkube create test --git-uri https://github.com/lreimer/testkube-zap-demo.git --git-branch main --type "zap/api" --name zap-api-git-test
kubectl testkube run test --args src/test/zap/zap-api-test.yaml --watch zap-api-git-test
```

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the MIT open source license, read the `LICENSE`
file for details.
