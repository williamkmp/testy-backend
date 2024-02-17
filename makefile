rebuild:
	docker rmi testy-backend-app --force && docker compose up -d
	docker network connect motionapi testy-backend-app-1
