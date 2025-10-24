# Releasing

1. Submit a PR with the following changes (see e.g. [#20](https://github.com/collectiveidea/twirp-kmp/pull/20)):
    * Update `version` in `gradle.properties` to the release version.
    * Update the `CHANGELOG.md`. Update the unreleased header to be the release version and make a new unreleased header.
    * Update the `README.md` as necessary to reflect the new release version.

2. Once merged, pull latest into main locally. Tag version:

   ```
   $ git tag -am "Version X.Y.Z" X.Y.Z
   ```

3. Push tags

   ```
   git push --tags
   ```

4. Build (generator)

   ```
   $ ./gradlew build
   ```

5. Create GitHub Release
   1. Visit the [New Releases](https://github.com/collectiveidea/twirp-kmp/releases/new) page.
   2. Supply release version and changelog link
   3. Upload `generator/build/libs/twirp-kmp-generator-X.Y.Z.jar` artifact.

6. Publish (runtime)

   ```
   $ ./gradlew publish
   ```

7. Promote from Staging

   ```
   source ~/.gradle/gradle.properties
   repository_key=$(curl -u $SONATYPE_USERNAME:$SONATYPE_PASSWORD -X GET "https://ossrh-staging-api.central.sonatype.com/manual/search/repositories?ip=any&profile_id=com.collectiveidea" | jq --raw-output ".repositories[0].key")
   curl -u $SONATYPE_USERNAME:$SONATYPE_PASSWORD -X POST "https://ossrh-staging-api.central.sonatype.com/manual/upload/repository/$repository_key"
   ```

8. Visit https://central.sonatype.com/publishing/deployments and press Publish
