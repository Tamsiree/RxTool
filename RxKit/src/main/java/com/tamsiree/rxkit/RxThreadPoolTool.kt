package com.tamsiree.rxkit

import java.util.concurrent.*

/**
 *
 * @author tamsiree
 * @date 2016/1/24
 * 线程池相关工具类
 */
class RxThreadPoolTool(type: Type?, corePoolSize: Int) {
    private var exec: ExecutorService? = null
    private val scheduleExec: ScheduledExecutorService

    /**
     * 在未来某个时间执行给定的命令
     *
     * 该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。
     *
     * @param command 命令
     */
    fun execute(command: Runnable?) {
        exec!!.execute(command)
    }

    /**
     * 在未来某个时间执行给定的命令链表
     *
     * 该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。
     *
     * @param commands 命令链表
     */
    fun execute(commands: List<Runnable?>) {
        for (command in commands) {
            exec!!.execute(command)
        }
    }

    /**
     * 待以前提交的任务执行完毕后关闭线程池
     *
     * 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。
     * 如果已经关闭，则调用没有作用。
     */
    fun shutDown() {
        exec!!.shutdown()
    }

    /**
     * 试图停止所有正在执行的活动任务
     *
     * 试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。
     *
     * 无法保证能够停止正在处理的活动执行任务，但是会尽力尝试。
     *
     * @return 等待执行的任务的列表
     */
    fun shutDownNow(): List<Runnable> {
        return exec!!.shutdownNow()
    }

    /**
     * 判断线程池是否已关闭
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    val isShutDown: Boolean
        get() = exec!!.isShutdown

    /**
     * 关闭线程池后判断所有任务是否都已完成
     *
     * 注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    val isTerminated: Boolean
        get() = exec!!.isTerminated

    /**
     * 请求关闭、发生超时或者当前线程中断
     *
     * 无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行。
     *
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @return `true`: 请求成功<br></br>`false`: 请求超时
     * @throws InterruptedException 终端异常
     */
    @Throws(InterruptedException::class)
    fun awaitTermination(timeout: Long, unit: TimeUnit?): Boolean {
        return exec!!.awaitTermination(timeout, unit)
    }

    /**
     * 提交一个Callable任务用于执行
     *
     * 如果想立即阻塞任务的等待，则可以使用`result = exec.submit(aCallable).get();`形式的构造。
     *
     * @param task 任务
     * @param <T>  泛型
     * @return 表示任务等待完成的Future, 该Future的`get`方法在成功完成时将会返回该任务的结果。
    </T> */
    fun <T> submit(task: Callable<T>?): Future<T> {
        return exec!!.submit(task)
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task   任务
     * @param result 返回的结果
     * @param <T>    泛型
     * @return 表示任务等待完成的Future, 该Future的`get`方法在成功完成时将会返回该任务的结果。
    </T> */
    fun <T> submit(task: Runnable?, result: T): Future<T> {
        return exec!!.submit(task, result)
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task 任务
     * @return 表示任务等待完成的Future, 该Future的`get`方法在成功完成时将会返回null结果。
     */
    fun submit(task: Runnable?): Future<*> {
        return exec!!.submit(task)
    }

    /**
     * 执行给定的任务
     *
     * 当所有任务完成时，返回保持任务状态和结果的Future列表。
     * 返回列表的所有元素的[Future.isDone]为`true`。
     * 注意，可以正常地或通过抛出异常来终止已完成任务。
     * 如果正在进行此操作时修改了给定的 collection，则此方法的结果是不确定的。
     *
     * @param tasks 任务集合
     * @param <T>   泛型
     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同，每个任务都已完成。
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务。
    </T> */
    @Throws(InterruptedException::class)
    fun <T> invokeAll(tasks: Collection<Callable<T>?>?): List<Future<T>> {
        return exec!!.invokeAll(tasks)
    }

    /**
     * 执行给定的任务
     *
     * 当所有任务完成或超时期满时(无论哪个首先发生)，返回保持任务状态和结果的Future列表。
     * 返回列表的所有元素的[Future.isDone]为`true`。
     * 一旦返回后，即取消尚未完成的任务。
     * 注意，可以正常地或通过抛出异常来终止已完成任务。
     * 如果此操作正在进行时修改了给定的 collection，则此方法的结果是不确定的。
     *
     * @param tasks   任务集合
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @param <T>     泛型
     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同。如果操作未超时，则已完成所有任务。如果确实超时了，则某些任务尚未完成。
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务
    </T> */
    @Throws(InterruptedException::class)
    fun <T> invokeAll(tasks: Collection<Callable<T>?>?, timeout: Long, unit: TimeUnit?): List<Future<T>> {
        return exec!!.invokeAll(tasks, timeout, unit)
    }

    /**
     * 执行给定的任务
     *
     * 如果某个任务已成功完成（也就是未抛出异常），则返回其结果。
     * 一旦正常或异常返回后，则取消尚未完成的任务。
     * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。
     *
     * @param tasks 任务集合
     * @param <T>   泛型
     * @return 某个任务返回的结果
     * @throws InterruptedException 如果等待时发生中断
     * @throws ExecutionException   如果没有任务成功完成
    </T> */
    @Throws(InterruptedException::class, ExecutionException::class)
    fun <T> invokeAny(tasks: Collection<Callable<T>?>?): T {
        return exec!!.invokeAny(tasks)
    }

    /**
     * 执行给定的任务
     *
     * 如果在给定的超时期满前某个任务已成功完成（也就是未抛出异常），则返回其结果。
     * 一旦正常或异常返回后，则取消尚未完成的任务。
     * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。
     *
     * @param tasks   任务集合
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @param <T>     泛型
     * @return 某个任务返回的结果
     * @throws InterruptedException 如果等待时发生中断
     * @throws ExecutionException   如果没有任务成功完成
     * @throws TimeoutException     如果在所有任务成功完成之前给定的超时期满
    </T> */
    @Throws(InterruptedException::class, ExecutionException::class, TimeoutException::class)
    fun <T> invokeAny(tasks: Collection<Callable<T>?>?, timeout: Long, unit: TimeUnit?): T {
        return exec!!.invokeAny(tasks, timeout, unit)
    }

    /**
     * 延迟执行Runnable命令
     *
     * @param command 命令
     * @param delay   延迟时间
     * @param unit    单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其`get()`方法在完成后将返回`null`
     */
    fun schedule(command: Runnable?, delay: Long, unit: TimeUnit?): ScheduledFuture<*> {
        return scheduleExec.schedule(command, delay, unit)
    }

    /**
     * 延迟执行Callable命令
     *
     * @param callable 命令
     * @param delay    延迟时间
     * @param unit     时间单位
     * @param <V>      泛型
     * @return 可用于提取结果或取消的ScheduledFuture
    </V> */
    fun <V> schedule(callable: Callable<V>?, delay: Long, unit: TimeUnit?): ScheduledFuture<V> {
        return scheduleExec.schedule(callable, delay, unit)
    }

    /**
     * 延迟并循环执行命令
     *
     * @param command      命令
     * @param initialDelay 首次执行的延迟时间
     * @param period       连续执行之间的周期
     * @param unit         时间单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其`get()`方法在取消后将抛出异常
     */
    fun scheduleWithFixedRate(command: Runnable?, initialDelay: Long, period: Long, unit: TimeUnit?): ScheduledFuture<*> {
        return scheduleExec.scheduleAtFixedRate(command, initialDelay, period, unit)
    }

    /**
     * 延迟并以固定休息时间循环执行命令
     *
     * @param command      命令
     * @param initialDelay 首次执行的延迟时间
     * @param delay        每一次执行终止和下一次执行开始之间的延迟
     * @param unit         时间单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其`get()`方法在取消后将抛出异常
     */
    fun scheduleWithFixedDelay(command: Runnable?, initialDelay: Long, delay: Long, unit: TimeUnit?): ScheduledFuture<*> {
        return scheduleExec.scheduleWithFixedDelay(command, initialDelay, delay, unit)
    }

    enum class Type {
        FixedThread, CachedThread, SingleThread
    }

    /**
     * ThreadPoolUtils构造函数
     *
     * @param type         线程池类型
     * @param corePoolSize 只对Fixed和Scheduled线程池起效
     */
    init {
        // 构造有定时功能的线程池
        // ThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE, 10L, TimeUnit.MILLISECONDS, new BlockingQueue<Runnable>)
        scheduleExec = Executors.newScheduledThreadPool(corePoolSize)
        exec = when (type) {
            Type.FixedThread ->                 // 构造一个固定线程数目的线程池
                // ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new
                // LinkedBlockingQueue<Runnable>());
                Executors.newFixedThreadPool(corePoolSize)
            Type.SingleThread ->                 // 构造一个只支持一个线程的线程池,相当于newFixedThreadPool(1)
                // ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
                Executors.newSingleThreadExecutor()
            Type.CachedThread ->                 // 构造一个缓冲功能的线程池
                // ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
                Executors.newCachedThreadPool()
            else -> scheduleExec
        }
    }
}