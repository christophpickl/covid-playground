# functional lingo

* typeclass
* higher order functions
* higher kinded types
* ADT
* monad (category theory)
* monoid: e.g. (0 and plus), (1 and multiplication), (empty string and string concatenation), (true and &&), (false and ||)
* functor
* partial application
* currying
* referential transparency
* isomorphism

// arrow => merge of kategory and funktionale
// monad => wrapper classs (factory method, map, flatMap)
// typeclasses => Monoid, Semigroup, Semiring

// Arrow = { Core, FX, Optics, Meta }
// https://medium.com/beingprofessional/understanding-functor-and-monad-with-a-bag-of-peanuts-8fa702b3f69e

/*
error handling: ApplicativeError, MonadError
computation: Functor, Applicative, Monad, Bimonad, Comonad
folding: Foldable, Traverse
combining: Semigroup, SemigroupK, Monoid, MonoidK
effects: MonadDefer, Async, Effect
recursion: Recursive, BiRecurisve
MTL: FunctorFilter, MonadState, MonadReader, MonadWriter, MonadFilter

error handling: Option, Try, Validated, Either, Ior
RWS: Reader, Writer, State
collections: ListK, SequenceK, MapK, SetK
transfomers: ReaderT, WriterT, OptionT, StateT, EitherT
evaluation: Eval, Trampoline, Free, FunctionN
effects: IO, Free, ObservableK
optics: Lens, Prism, Iso
recursion: Fix, Mu, Nu
others: Coproduct, Coreader, Const
*/