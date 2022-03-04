import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Promise

fun main() {
    window.setTimeout(::foo1, 0);
    window.setTimeout(::foo2, 0);
}

fun charPromise(c: Char): Promise<Char?> {
    return GlobalScope.promise {
        return@promise c;
    }
}


suspend fun <T> Promise<T>.await(): T = suspendCoroutine { cont ->
    then({ cont.resume(it) }, { cont.resumeWithException(it) })
}

fun foo1(): Promise<Unit> {
    return GlobalScope.promise {
        val it = charPromise('!').await();
        if (it != null) {
            console.log(it == '!')
        };
    }
}

fun foo2(): Promise<Unit> {
    return GlobalScope.promise {
        charPromise('!').then {
            if (it != null) {
                console.log(it == '!');
            }
        }
    }
}

