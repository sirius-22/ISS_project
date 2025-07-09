## TF2025 Requirements

The company asks us to build a software system (named ${\color{blue}\text{cargoservice}}$) that:

1. **Receives a request to load** on the cargo a product container already registered in the ${\color{blue}\text{productservice}}$.

   The request is rejected when:

   * the product weight is evaluated too high, since the ship can carry a maximum load of
     ${\color{black}\texttt{MaxLoad > 0 kg}}$
   * the hold is already full, i.e., the
     ${\color{lightblue}\texttt{4 slots}}$ are already occupied.

   If the request is accepted, the ${\color{blue}\text{cargoservice}}$ associates a slot to the product
   ${\color{black}\texttt{PID}}$ and returns the name of the reserved slot.
   Afterwards, it waits for the product container to be delivered to the ${\color{blue}\text{ioport}}$.
   In the meantime, other requests are not elaborated.

2. Detects (by means of the ${\color{blue}\text{sonar}}$ ${\color{magenta}\text{sensor}}$) the presence of the product container at the ${\color{blue}\text{ioport}}$.

3. Ensures that the product container is placed by the ${\color{blue}\text{cargorobot}}$ within its reserved slot.
   At the end of the operation:

   * the ${\color{blue}\text{cargorobot}}$ should return to its ${\color{brown}\text{HOME}}$ location;
   * the ${\color{blue}\text{cargoservice}}$ can process another *load-request*.

4. Shows the current state of the ${\color{blue}\text{hold}}$, by means of a dynamically updated ${\color{magenta}\text{web-GUI}}$.

5. ${\color{brown}\text{Interrupts}}$ any activity and turns on a LED if the ${\color{blue}\text{sonar sensor}}$ measures a distance
   $D > D_{FREE}$
   for at least \$3\$ seconds (possibly a sonar failure).
   The service continues its activities as soon as the sonar measures a distance
   $D \leq D_{FREE}$


