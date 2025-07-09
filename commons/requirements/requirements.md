<h2>TF2025 Requirements<a class="headerlink" href="#tf2025-requirements" title="Permalink to this heading">¶</a></h2>
<p>The company asks us to build a software systems (named <span class="blue">cargoservice</span>) that:</p>
<ol class="arabic">
<li><p>is able to receive the <strong>request to load</strong> on the cargo a product container already registered in the <span class="blue">productservice</span>.</p>
<p>The request is rejected when:</p>
<ul class="simple">
<li><p>the product-weight is evaluated too high, since the ship can carry a maximum load of <code class="docutils literal notranslate"><span class="pre">MaxLoad&gt;0</span>&#160; <span class="pre">kg</span></code>.</p></li>
<li><p>the  hold is already full, i.e. the <code class="docutils literal notranslate"><span class="pre">4</span> <span class="pre">slots</span></code> are alrready occupied.</p></li>
</ul>
<p>If the request is accepted, the <span class="blue">cargoservice</span> associates a slot to the product <code class="docutils literal notranslate"><span class="pre">PID</span></code> and returns the name
of the reserved slot. Afttwerds, it waits that the  product container is delivered to the <span class="blue">ioport</span>.
In the meantime, other requests are not elaborated.</p>
</li>
<li><p>is able to detect (by means of the <span class="blue">sonar</span> <span class="slide3">sensor</span>) the presence of the product container at the <span class="blue">ioport</span></p></li>
<li><p>is able  to ensure that the product container is placed by the  <span class="blue">cargorobot</span> within its reserved slot.
At the end of the  work:</p>
<ul class="simple">
<li><p>the  <span class="blue">cargorobot</span> should returns to its <span class="brown">HOME</span> location.</p></li>
<li><p>the <span class="blue">cargoservice</span>  can process another <em>load-request</em></p></li>
</ul>
</li>
<li><p>is able to show the current state of the <span class="blue">hold</span>, by mesans of a dynamically updated <span class="slide3">web-gui</span>.</p></li>
<li><p><span class="brown">interrupts</span> any activity and turns on a led if the <span class="blue">sonar sensor</span> measures a distance <code class="docutils literal notranslate"><span class="pre">D</span> <span class="pre">&gt;</span> <span class="pre">DFREE</span></code>
for at least <code class="docutils literal notranslate"><span class="pre">3</span></code> secs (perhaps a sonar failure). The service continues its activities as soon as the sonar
measures a distance <code class="docutils literal notranslate"><span class="pre">D</span> <span class="pre">&lt;=</span> <span class="pre">DFREE</span></code>.</p></li>
</ol>
