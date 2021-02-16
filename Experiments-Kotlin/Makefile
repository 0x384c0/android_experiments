PROJECT_DIR_ROOT="Application/"
PROJECT_DIR="Application/app/"
PROJECT_CORE_DIR="CoreNetwork/"

all:
	$(MAKE) check_env
	$(MAKE) clean

clean:
	cd $(PROJECT_DIR)  &&\
	./gradlew clean

check_env:
	sh scripts/check_env.sh

increment_version:
	APP_VERSION=$$(sh scripts/get_version_from_tag.sh) &&\
	NEW_APP_VERSION=$$(sh scripts/incr_version.sh $$APP_VERSION)  &&\
	sh scripts/set_version.sh $$NEW_APP_VERSION $(PROJECT_DIR) &&\
	sh scripts/set_version.sh $$NEW_APP_VERSION $(PROJECT_CORE_DIR) &&\
	echo "# When done, enter commands:" &&\
	echo "	git add \"$(PROJECT_DIR)build.gradle\" \"$(PROJECT_CORE_DIR)build.gradle\"" &&\
	echo "	git commit -m \"version $$NEW_APP_VERSION\"" &&\
	echo "" && echo "# then:" &&\
	echo "	git flow release start \"$$NEW_APP_VERSION\"" &&\
	echo "	export GIT_MERGE_AUTOEDIT=no && git flow release finish -m \"Merged $$NEW_APP_VERSION\" \"$$NEW_APP_VERSION\" && unset GIT_MERGE_AUTOEDIT" &&\
	echo "	git push --all && git push --tags" &&\
	echo "" && echo "# or:" &&\
	echo "	git tag $$NEW_APP_VERSION" &&\
	echo "	git push --all && git push --tags" &&\
	echo "	"

beta:
	#make beta FABRIC_API_TOKEN=<token> FABRIC_BUILD_SECRET=<secret>
	export LC_ALL="en_US.UTF-8" &&\
	export FABRIC_API_TOKEN="$(FABRIC_API_TOKEN)" &&\
	export FABRIC_BUILD_SECRET="$(FABRIC_BUILD_SECRET)" &&\
	cd "$(PROJECT_DIR_ROOT)" &&\
	bundle install &&\
	bundle exec fastlane beta

buildAAB:
	export LC_ALL="en_US.UTF-8" &&\
	cd "$(PROJECT_DIR_ROOT)" &&\
	bundle install &&\
	bundle exec fastlane buildAAB


buildAPK:
	echo "\n\nUsage" &&\
	echo '	get token from https://api.telegram.org/bot<TOKEN>/getUpdates' &&\
	echo '	make buildAPK &&\' &&\
    echo '	PREV_VERSION=$(cat ~/android_artifacts/LAST_VERSION.txt || true) &&\' &&\
    echo '	CURRENT_VERSION=$(git tag | tail -n 1) &&\' &&\
    echo '	CHANGELOG=$(git log "$$PREV_VERSION"..HEAD --pretty=format:"%s") &&\' &&\
    echo '	echo $$CURRENT_VERSION > ~/android_artifacts/LAST_VERSION.txt &&\' &&\
	echo '	curl -v -F "chat_id=<CHAT_ID>" -F document="@$$(ls ~/android_artifacts/app-beta.apk)" -F caption="Version $$CURRENT_VERSION \nChangelog:\n$$CHANGELOG" https://api.telegram.org/bot<TOKEN>/sendDocument' &&\
	export LC_ALL="en_US.UTF-8" &&\
	cd "$(PROJECT_DIR_ROOT)" &&\
	bundle install &&\
	bundle exec fastlane buildAPK

merge_current_to_ci_develop:
	CURRENT_BRANCH=$$(git rev-parse --abbrev-ref HEAD) &&\
	git checkout ci_develop &&\
	git pull --rebase origin ci_develop &&\
	git merge $$CURRENT_BRANCH &&\
	git checkout $$CURRENT_BRANCH &&\
	git push --all origin



# Utils
generate_ssl_hash:
	sh scripts/generate_ssl_hash.sh "https://api.google.com"

enable_wifi_adb:
	sh scripts/enable_wifi_adb.sh

generate_sms_retriever_app_hash_release:
	sh scripts/sms_retriever_hash_v9.sh --package "com.0x348c0.app" --keystore "app/apk_signing/release.keystore" --keystore_password "KEYSTORE_PASSWORD"


generate_sms_retriever_app_hash_beta:
	sh scripts/sms_retriever_hash_v9.sh --package "com.0x348c0.app.beta" --keystore "app/apk_signing/beta.keystore" --keystore_password "KEYSTORE_PASSWORD"