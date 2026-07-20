package com.collectiveidea.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

/**
 * Clears the cached bearer token so the Auth plugin invokes its `loadTokens` block again on the
 * next request — useful when the token has changed out from under the client, e.g. on logout.
 *
 * As of Ktor 3.4.0, `BearerAuthProvider.clearToken()` is the documented, supported API for this.
 * See: https://ktor.io/docs/client-auth.html
 */
public fun HttpClient.invalidateBearerTokens() {
    authProvider<BearerAuthProvider>()?.clearToken()
}
