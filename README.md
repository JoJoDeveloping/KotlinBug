# A bug in the Kotlin->JS legacy compiler

## A very short description of the bug

Chars (and maybe other primitives) are not properly unboxed when used in a coroutine.

## How to test this bug

execute `gradle run`. Your browser should open the page. Then, open the JS console. You should see two log outputs. If you use the legacy JS compiler, one will say `true`, the other will say `false`.

## A longer explaination

In `Simple.kt`, you can find two methods: `foo1()` and `foo2()`. Both produce a promise. Both wait for a promised `Char?` and then compare the resulting char to the expected value. The difference is:
* One method uses `Promise#then` to pass the result of the promise to a consumer.
* One await's the promise using `suspendCoroutine`, exploiting Kotlin's coroutine support.

In the second case, the compiler incorrectly forgets to unbox the `Char?` to a `Char`, causing the comparsion to return false since the runtime value of `it` does not match the supposed static type, as we compare a JS object with a JS number.

The bug goes away when using the `IR` compiler instead of `LEGACY`.
