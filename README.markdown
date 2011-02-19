Philosophy
==========
Imposing arbitary rules on user passwords has failed.  Rather than invent
c0Mpl3x pazwrds, users add 1s or start speaking 133t.  Little security is
gained as attackers can easily guess these workaround.  We should accept that
given 50,000 or so attempts, all memorable passwords are weak.  Let's make sure
attackers can't have those 50,000 chances, and concentrate on robust methods
for screening out predicable passwords.  If the most common password is now 'Password1!', we have failed.

Method
======
ProbPass does not have fixed rules or a bad passwords list.  Potential passwords are evaluated by the likelyhood of them appearing in a given population of passwords.  This population varies between sites, 'twitter' is a predictably common string in Twitter passwords, but not for on-line banking.

These populations are generated from wordlists [in the formats used by SkullSecurity] and can be pre-computed.  Individual services can merge in populations based on their own common passwords.  For example, a stock broker would want to indicate 'bull', 'shares', etc as being more common.  This can also be done per password check, to avoid users basing passwords on their email address or login name.

Useage
======
ProbPass can be used in its default configuration from the command-line like so:

	$ java -jar probpass.jar 11
	letmein
	FAIL
	letmein

The 11 is an optional strictness setting [bigger is stricter]. A failure results in an exit code of 2, 'FAIL' on stderr and the failed password on stdout.

Usually, ProbPass expects to be used from Spring and has clean Spring integration.

	<beans	xmlns="http://www.springframework.org/schema/beans"
	        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	        xsi:schemaLocation="
	            http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
	            http://www.github.com/tbarker/probpass http://www.github.com/tbarker/probpass/schema/probpass.xsd"
	        xmlns:pass="http://www.github.com/tbarker/probpass"
	>
	    <pass:checker id="test" strictness="6" l33t="false">
		   <pass:population weight="0.05">
		       <pass:sample>kitten</pass:sample>
		       <pass:sample>cat</pass:sample>
		       <pass:sample>mobydick</pass:sample>
		   </pass:population>
		   <pass:population weight="0.55" file="test/tuscl.wordcount" />
	       <pass:defaultPopulation weight="0.35" />
		</pass:checker>
	</beans>

&lt;population> elements can be used to load files with extensions of:

 * wordlist - Standard Unix wordlists
 * wordcount - [SkullSecurity format](http://www.skullsecurity.org/wiki/index.php/Passwords) counted wordlists
 * ser - Serialised ProbPass populations.  (See the class GenerateFromWords.)

Using the &lt;defaultPopulation> or leaving &lt;checker> empty includes the default ProbPass population.  (Based on [The Ultimate Strip Club password leak](http://downloads.skullsecurity.org/passwords/tuscl.txt).)

The l33t attribute switches the de-133tifying pre-processing.  This treates 'pa455w4d' as 'password'.

Once a ProbPass checker has been configured, you can call it from your code like this.

	List<String> userSpecific = new ArrayList<String>();
	userSpecific.add( "joeuser" );
	userSpecific.add( "joe73@hotmail.com" );

	boolean passwordIsGood = checker.check( "lipsolordinum", userSpecific ) );


Credits
=======
 * [Skullsecurity](http://www.skullsecurity.org/blog/), without his leaked password lists I would never have been able to know if this would work.
 * [Measuring Password Strength: An Empirical Analysisi](http://arxiv.org/abs/0907.3402).  A great academic paper on what passwords really look like.
 * Google for Guava, some bits of which are included in this project.  (Took the source needed, rather than include a whole JAR.)  Beautifully coded. 


TODO
====
 * Better default population.  Maybe by combining several populations from password leaks using geometric weights.
 * Formalised likelyhood model.  I looked into using Malkov Chains for this, but gave up after they took literally days to generate. With a formal model, password strength could be measure as average number of attempts to break.  That would allow cost/benefit analysis of strictness.
 * Integration to Guice (and maybe GWT).
 * A real command-line interface for offline password analysis.
 * Ant tasks for generating site specific populations from static content.

