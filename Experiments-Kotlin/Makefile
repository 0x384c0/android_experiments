PROJECT_DIR="Application/"

all:
	#make LOGIN_KEYCHAN_PASSWORD=<osx_login_pass>
	# setup
	$(MAKE) check_env
	$(MAKE) clean

clean:
	cd $(PROJECT_DIR)  &&\
	./gradlew clean

check_env:
	sh scripts/check_env.sh

increment_version:
	APP_VERSION=$$(sh scripts/get_version_from_tag.sh) &&\
	source scripts/incr_version.sh $$APP_VERSION  &&\
	sh scripts/set_version.sh $$NEW_APP_VERSION &&\
	rm -f "$(PROJECT_DIR)app/build.gradlebak"  &&\
	echo "# When done, enter commands:" &&\
	echo "	git add \"$(PROJECT_DIR)app/build.gradle\"" &&\
	echo "	git commit -m \"version $$NEW_APP_VERSION\"" &&\
	echo "" && echo "# then:" &&\
	echo "	git flow release start \"$$NEW_APP_VERSION\"" &&\
	echo "	export GIT_MERGE_AUTOEDIT=no && git flow release finish -m \"Merged $$NEW_APP_VERSION\" \"$$NEW_APP_VERSION\" && unset GIT_MERGE_AUTOEDIT" &&\
	echo "	git push --all && git push --tags" &&\
	echo "" && echo "# or:" &&\
	echo "	git tag $$NEW_APP_VERSION" &&\
	echo "	git push --all && git push --tags" &&\
	echo "	"

create_release:
	export GIT_MERGE_AUTOEDIT=no &&\
	APP_VERSION=$$(sh scripts/get_version_from_tag.sh) &&\
	source scripts/incr_version.sh $$APP_VERSION  &&\
	git flow release start "$$NEW_APP_VERSION"  &&\
	sh scripts/set_version.sh $$NEW_APP_VERSION  &&\
	rm -f "$(PROJECT_DIR)app/build.gradlebak"  &&\
	git add "$(PROJECT_DIR)app/build.gradle"  &&\
	git commit -m "version $$NEW_APP_VERSION"  &&\
	git flow release finish -m "version $$NEW_APP_VERSION" "$$NEW_APP_VERSION" &&\
	unset GIT_MERGE_AUTOEDIT &&\
	git push --all &&\
	git push --tags

beta:
	#make beta FABRIC_API_TOKEN=<token> FABRIC_BUILD_SECRET=<secret>
	export LC_ALL="en_US.UTF-8" &&\
	export FABRIC_API_TOKEN="$(FABRIC_API_TOKEN)" &&\
	export FABRIC_BUILD_SECRET="$(FABRIC_BUILD_SECRET)" &&\
	cd "$(PROJECT_DIR)" &&\
	bundle install &&\
	bundle exec fastlane beta

buildAAB:
	export LC_ALL="en_US.UTF-8" &&\
	cd "$(PROJECT_DIR)" &&\
	bundle install &&\
	bundle exec fastlane buildAAB


buildAPK:
	echo "\n\nUsage" &&\
	echo '	get token from https://api.telegram.org/bot<TOKEN>/getUpdates' &&\
	echo '	make buildAPK &&\' &&\
    echo '	PREV_VERSION=$(cat ~/Downloads/android_artifacts/LAST_VERSION.txt || true) &&\' &&\
    echo '	CURRENT_VERSION=$(git tag | tail -n 1) &&\' &&\
    echo '	CHANGELOG=$(git log "$$PREV_VERSION"..HEAD --pretty=format:"%s") &&\' &&\
    echo '	echo $$CURRENT_VERSION > ~/Downloads/android_artifacts/LAST_VERSION.txt &&\' &&\
	echo '	curl -v -F "chat_id=<CHAT_ID>" -F document="@$$(ls ~/Downloads/android_artifacts/app-beta.apk)" -F caption="Version $$CURRENT_VERSION \nChangelog:\n$$CHANGELOG" https://api.telegram.org/bot<TOKEN>/sendDocument' &&\
	export LC_ALL="en_US.UTF-8" &&\
	cd "$(PROJECT_DIR)" &&\
	bundle install &&\
	bundle exec fastlane buildAPK

merge_current_to_ci_develop:
	CURRENT_BRANCH=$$(git rev-parse --abbrev-ref HEAD) &&\
	git checkout ci_develop &&\
	git pull --rebase origin ci_develop &&\
	git merge $$CURRENT_BRANCH &&\
	git checkout $$CURRENT_BRANCH &&\
	git push --all origin
