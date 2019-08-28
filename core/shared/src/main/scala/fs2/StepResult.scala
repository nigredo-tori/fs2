package fs2

/** Result of stepping a pull. */
private[fs2] sealed abstract class StepResult[F[_], H[_], O, R]

private[fs2] object StepResult {

  /** The step reached the end of the pull. */
  final case class Done[F[_], H[_], O, R](result: R) extends StepResult[F, H, O, R]

  /** The step output a chunk of elements and has a subsequent tail pull. */
  final case class Output[F[_], H[_], O, R](scope: Scope[H], head: Chunk[O], tail: Pull[F, O, R])
      extends StepResult[F, H, O, R]

  /** The step was interrupted. */
  final case class Interrupted[F[_], H[_], O, R](err: Option[Throwable])
      extends StepResult[F, H, O, R]

  def done[F[_], H[_], O, R](result: R): StepResult[F, H, O, R] = Done(result)
  def output[F[_], H[_], O, R](scope: Scope[H],
                               head: Chunk[O],
                               tail: Pull[F, O, R]): StepResult[F, H, O, R] =
    Output(scope, head, tail)
  def interrupted[F[_], H[_], O, R](err: Option[Throwable]): StepResult[F, H, O, R] =
    Interrupted(err)
}
