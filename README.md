# Continuous OpenAPI Security Tests on K8s with Testkube and ZAP

Demo repository for the DevSecCon 22 Lightning Talk on Continuous OpenAPI Security Tests on K8s with Testkube and ZAP.

## Bootstrapping

For a simple local setup (e.g. on Rancher Desktop) use the following instructions:

```bash
# the ZAP setup
kubectl apply -f k8s/zap/zap-namespace.yaml 
kubectl apply -f k8s/zap/
kubectl get all -n zap
kubectl port-forward pod/zap-webswing -n zap 8080:8080
kubectl port-forward pod/zap-daemon -n zap 9080:9080

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

# creating and ZAP tests
kubectl testkube create test --file tests/zap-api-test.yaml --type "zap/api" --name zap-api-test
kubectl testkube run test --watch zap-api-test
```

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the MIT open source license, read the `LICENSE`
file for details.
