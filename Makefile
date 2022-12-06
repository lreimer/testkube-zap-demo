GITHUB_USER ?= lreimer

prepare-gke-cluster:
	@gcloud config set compute/zone europe-west1-b
	@gcloud config set container/use_client_certificate False

create-gke-cluster:
	@gcloud container clusters create testkube-zap-demo --num-nodes=5 --enable-autoscaling --min-nodes=5 --max-nodes=7 --cluster-version=1.23
	@kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$$(gcloud config get-value core/account)
	@kubectl cluster-info

bootstrap-flux2:
	@flux bootstrap github \
		--owner=$(GITHUB_USER) \
  		--repository=testkube-zap-demo \
  		--branch=main \
  		--path=./k8s/clusters/testkube-zap-demo \
		--components-extra=image-reflector-controller,image-automation-controller \
		--read-write-key \
  		--personal

delete-gke-cluster:
	@gcloud container clusters delete testkube-zap-demo --async --quiet
