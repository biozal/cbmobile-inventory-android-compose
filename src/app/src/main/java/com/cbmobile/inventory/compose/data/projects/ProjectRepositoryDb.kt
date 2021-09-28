package com.cbmobile.inventory.compose.data.projects

import android.content.Context
import java.util.*
import java.text.SimpleDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.couchbase.lite.*
import com.google.gson.Gson
import com.cbmobile.inventory.compose.data.InventoryDatabase
import com.cbmobile.inventory.compose.data.location.LocationRepository
import com.cbmobile.inventory.compose.models.Location
import com.cbmobile.inventory.compose.models.Project
import com.cbmobile.inventory.compose.models.ProjectModelDTO


@InternalCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
class ProjectRepositoryDb(private val inventoryDatabase: InventoryDatabase,
                          private val locationRepository: LocationRepository
)
    : ProjectRepository {

   override val projectDatabaseName
       get() = inventoryDatabase.getTeamProjectDatabaseName()

    override suspend fun getProject(projectId: String): Project {
        return withContext(Dispatchers.IO){
            try {
                val db = inventoryDatabase.databases[inventoryDatabase.projectDatabaseName]?.database
                db?.let { database ->
                    val doc = database.getDocument(projectId)
                    doc?.let { document  ->
                        return@withContext Gson().fromJson(document.toJSON(), Project::class.java)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext Project(projectId = projectId, createdOn = Date(), modifiedOn = Date())
        }
    }

    fun getProjectDocumentChangeFlow(documentId: String) : Flow<Project?>?{
        val db =
            inventoryDatabase.databases[inventoryDatabase.projectDatabaseName]?.database
        db?.let { database ->
            return database.documentChangeFlow(documentId)
                .map { dc -> mapDocumentChangeToProject(dc) }
                .flowOn(Dispatchers.IO)
        }
        return null
    }

    private fun mapDocumentChangeToProject(documentChange: DocumentChange) : Project?{
        var project: Project? = null
        val db =
            inventoryDatabase.databases[inventoryDatabase.projectDatabaseName]?.database
        db?.let { database ->
            val doc = database.getDocument(documentChange.documentID)
            doc?.let { document ->
                project = Gson().fromJson(document.toJSON(), Project::class.java)
            }
        }
        return project
    }

    override fun getProjectsFlow(): Flow<List<Project>>? {
        try {
            val db =
                inventoryDatabase.databases[inventoryDatabase.projectDatabaseName]?.database
            db?.let { database ->
                val query = database.createQuery("SELECT * FROM _ AS item WHERE type = \"project\"")
                val flow = query
                    .queryChangeFlow()
                    .map{ qc -> mapQueryChangeToProjects(qc)}
                    .flowOn(Dispatchers.IO)
                query.execute()
                return flow
            }
        } catch (e: Exception){
            android.util.Log.e(e.message, e.stackTraceToString())
        }
        return null
    }

    private fun mapQueryChangeToProjects(queryChange: QueryChange) : List<Project>{
        val projects = mutableListOf<Project>()
        queryChange.results?.let { results ->
            results.forEach() { result ->
                projects.add(Gson().fromJson(result.toJSON(), ProjectModelDTO::class.java).item)
            }
        }
        return projects
    }

    override suspend fun saveProject(project: Project) {
        return withContext(Dispatchers.IO) {
           try{
               val db = inventoryDatabase.databases[inventoryDatabase.projectDatabaseName]?.database
               db?.let { database ->
                   val json = Gson().toJson(project)
                   val doc = MutableDocument(project.projectId, json)
                   database.save(doc)
               }
           } catch (e: Exception){
               android.util.Log.e(e.message, e.stackTraceToString())
           }
        }
    }

    override suspend fun deleteProject(projectId: String) : Boolean {
        return withContext(Dispatchers.IO){
            var result = false
            try {
                val db =
                    inventoryDatabase.databases[inventoryDatabase.projectDatabaseName]?.database
                db?.let { database ->
                    val projectDoc = database.getDocument(projectId)
                    projectDoc?.let { document ->
                        db.delete(document)
                        result = true
                    }
                }
            } catch (e: java.lang.Exception) {
                android.util.Log.e(e.message, e.stackTraceToString())
            }
            return@withContext result
        }
    }

    override suspend fun completeProject(projectId: String) {
        return withContext(Dispatchers.IO){
            try{
                val db = inventoryDatabase.databases[inventoryDatabase.projectDatabaseName]?.database
                db?.let { database ->
                    val doc = database.getDocument(projectId)
                    doc?.let { document ->
                        val mutDoc = document.toMutable()
                        mutDoc.setBoolean("complete", true)
                        database.save(mutDoc)
                    }
                }
            } catch(e: java.lang.Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
        }
    }

    override suspend fun deleteDatabase() {
        return withContext(Dispatchers.IO){
            return@withContext inventoryDatabase.deleteDatabase()
        }
    }

    override suspend fun initializeDatabase() {
        return withContext(Dispatchers.IO) {
            return@withContext inventoryDatabase.initializeDatabase()
        }
   }

    override suspend fun loadSampleData() {
        return withContext(Dispatchers.IO){
            try {
                inventoryDatabase.loggedInUser?.let {
                    var locations = locationRepository.getLocations()
                    var locationLength = locations.count() - 1
                    saveProject(Project(projectId = UUID.randomUUID().toString(), name = "Audit ${ (1..1000000).random() }", description = getRandomDescription()
                        ,isComplete = false, type = "project", dueDate = SimpleDateFormat("MM-dd-yyyy").parse("${(1..12).random()}-${(1..27).random()}-${(2021..2023).random()}"), team = it.team,
                        createdBy = it.username, modifiedOn = Date(), modifiedBy = it.username, location = locations[(0..locationLength).random()]))

                    saveProject(Project(projectId = UUID.randomUUID().toString(), name = "Audit ${ (1..1000000).random() }", description = getRandomDescription()
                        ,isComplete = false, type = "project", dueDate = SimpleDateFormat("MM-dd-yyyy").parse("${(1..12).random()}-${(1..27).random()}-${(2021..2023).random()}"), team = it.team,
                        createdBy = it.username, modifiedOn = Date(), modifiedBy = it.username, location = locations[(0..locationLength).random()]))

                    saveProject(Project(projectId = UUID.randomUUID().toString(), name = "Audit ${ (1..1000000).random() }", description = getRandomDescription()
                        ,isComplete = false, type = "project", dueDate = SimpleDateFormat("MM-dd-yyyy").parse("${(1..12).random()}-${(1..27).random()}-${(2021..2023).random()}"), team = it.team,
                        createdBy = it.username, modifiedOn = Date(), modifiedBy = it.username, location = locations[(0..locationLength).random()]))

                    saveProject(Project(projectId = UUID.randomUUID().toString(), name = "Audit ${ (1..1000000).random() }", description = getRandomDescription()
                        ,isComplete = false, type = "project", dueDate = SimpleDateFormat("MM-dd-yyyy").parse("${(1..12).random()}-${(1..27).random()}-${(2021..2023).random()}"), team = it.team,
                        createdBy = it.username, modifiedOn = Date(), modifiedBy = it.username, location = locations[(0..locationLength).random()]))

                    saveProject(Project(projectId = UUID.randomUUID().toString(), name = "Audit ${ (1..1000000).random() }", description = getRandomDescription()
                        ,isComplete = false, type = "project", dueDate = SimpleDateFormat("MM-dd-yyyy").parse("${(1..12).random()}-${(1..27).random()}-${(2021..2023).random()}"), team = it.team,
                        createdBy = it.username, modifiedOn = Date(), modifiedBy = it.username, location = locations[(0..locationLength).random()]))
                }

            } catch (e: Exception){
                android.util.Log.e(e.message, e.stackTraceToString())
            }
        }
    }

    fun getRandomDescription() : String{
        var descriptions = mutableListOf("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
        descriptions.add("Empressr bubbli hulu twones meebo kiko yoono, hojoki mog eduvant airbnb kiko heekya, cotweet wufoo meebo yammer greplin. geni foodzie. Cuil yammer plaxo stypi flickr, diigo zimbra doostang blyve hipmunk, flickr heekya palantir. Kiko kno diigo udemy hipmunk zillow eskobo qeyno, dopplr mobly joost edmodo omgpop. Airbnb gsnap spock spotify, ngmoco wufoo. Yuntaa vimeo chartly joukuu unigo wesabe, kazaa appjet sifteo rovio. Movity mog meevee qeyno plugg, hipmunk imvu. Babblely skype empressr oovoo, udemy mobly. Kippt whrrl ebay dogster, mzinga eskobo heekya prezi, shopify plaxo. Nuvvo jabber sclipo kazaa sifteo yuntaa, imvu wikia eduvant. Woopra oooj loopt oooooc zoho, wikia vuvox. Chartly chegg foodzie twones, cloudera oooj. Elgg bitly hipmunk jabber movity chegg tivo, ideeli blippy ngmoco stypi.")
        descriptions.add("Yoono wakoopa boxbe zooomr, balihoo. Wufoo ngmoco chumby rovio, edmodo. Chartly knewton twones jibjab zoodles, boxbe babblely appjet koofers, babblely waze ifttt.   Koofers babblely waze ifttt orkut, kosmix chartly reddit, revver joyent spotify. Balihoo cuil chegg wikia trulia, geni weebly spotify jabber, zapier zinch unigo. Prezi kaboodle groupon jumo reddit, yuntaa vimeo chumby, bubbli kaboodle zoosk. Skype cuil mog jumo, whrrl mzinga. Qeyno klout blyve kosmix empressr, kiko grockit kazaa.")
        descriptions.add("Webtwo ipsum kosmix joukuu ebay. Bebo chartly xobni qeyno, rovio. Bebo woopra plugg flickr oooj boxbe zanga, jajah blekko yoono lanyrd. Wakoopa geni zinch elgg, tumblr. Greplin jaiku jiglu zanga plugg, prezi kosmix disqus. Quora vuvox chegg orkut, mzinga. Mog cotweet napster zoodles reddit, flickr unigo wesabe, babblely convore divvyshot. Napster loopt gooru joost klout chumby chegg blippy, gsnap wikia wufoo sococo bitly. Wakoopa plugg tumblr heekya jaiku unigo spotify, mobly dropio odeo convore. Chumby tumblr bitly ning twones zappos twitter, reddit loopt chegg tumblr. Stypi cloudera doostang trulia xobni knewton, jumo geni zillow ning. Imeem doostang convore heroku eduvant oooooc shopify, insala airbnb jumo zynga. Kazaa koofers bebo, oooooc.")
        descriptions.add("ebay. Oooooc xobni zoho voxy meebo omgpop, blyve gsnap oooj.  Gsnap oooj gooru insala orkut bebo, chegg blippy sclipo. Palantir oooooc cuil, skype. zynga kaboodle spock. Tivo airbnb revver cuil fleck, reddit octopart. Twitter jumo gooru zoosk joost, woopra prezi jibjab. Wakoopa spotify nuvvo jibjab chegg, elgg ebay eduvant. Twitter insala blyve kaboodle, sococo. Joost chartly waze babblely quora voxy, zlio blyve babblely skype. Zynga lala ebay loopt doostang, ning meebo doostang, ning airbnb dopplr. Skype appjet squidoo qeyno napster, unigo akismet heekya prezi, chartly zillow kaboodle.")
        descriptions.add("Jibjab loopt heroku reddit plickers jiglu weebly, whrrl divvyshot kazaa shopify. Udemy blippy chartly eskobo jiglu trulia bubbli plickers, revver sococo jumo heekya gooru. Sifteo hojoki jiglu vimeo, appjet. Chegg spock wakoopa ngmoco, cloudera hojoki. Omgpop heroku ning blippy ideeli meevee tivo oovoo, oooooc sclipo zynga jibjab unigo.")
        descriptions.add("Jajah empressr kippt kno wufoo zillow, lala jiglu voki akismet. Bubbli rovio ebay bubbli empressr zappos, reddit geni akismet lala. Ideeli squidoo zapier klout etsy koofers kazaa, zappos jumo dopplr qeyno. Balihoo stypi insala ifttt imeem dopplr, omgpop chartly rovio loopt appjet, gooru twones ning ebay. Chegg zimbra vimeo, woopra. Ning heroku zlio orkut greplin, kazaa zappos balihoo tumblr quora, bubbli geni zoodles. Jaiku whrrl zynga, edmodo.")
        descriptions.add("Lanyrd dogster imeem zinch jabber kiko, odeo eduvant twitter zapier. Airbnb kippt loopt oooooc, wikia ning. Sclipo wikia glogster stypi cotweet ideeli revver, dropio plugg cotweet zappos zanga joyent, zappos weebly hulu xobni mzinga.")
        descriptions.add("Joukuu cotweet oooj squidoo sococo, plaxo koofers zappos revver edmodo, stypi cloudera blekko. Disqus wikia lala mzinga, kiko kosmix, handango joost. Oooj bitly cotweet heekya twitter tumblr wakoopa dogster, twitter odeo akismet kiko lijit. Oovoo grockit dropio ngmoco kiko, hipmunk oooooc. Napster bebo xobni lanyrd zapier xobni kaboodle, yoono plickers kazaa wesabe. Jumo meebo mozy trulia zimbra kippt, kno hojoki klout. Revver sococo oooooc jaiku airbnb, divvyshot joyent. Shopify zinch lijit, twones.")
        descriptions.add("Nuvvo kiko eskobo plickers empressr, grockit airbnb. Oooj dogster jabber voxy oooooc, gooru chegg skype. Zoodles shopify kaboodle napster dropio gooru, reddit kno yuntaa bitly. Koofers wikia waze trulia joukuu plugg, divvyshot ebay yoono. Airbnb plaxo handango flickr, divvyshot zillow. tivo airbnb blekko. Movity twitter unigo xobni squidoo mzinga divvyshot palantir, vuvox voki wikia scribd zinch yoono. meevee plugg. Flickr balihoo cuil geni kazaa, akismet imvu twones glogster edmodo, twones plickers etsy.")
        descriptions.add("Mozy zillow xobni revver convore, foodzie imvu cuil. Tumblr reddit jiglu chartly blyve, oovoo bubbli. Doostang eskobo rovio movity hipmunk, kazaa elgg xobni, cotweet twitter fleck. Nuvvo ideeli diigo zapier orkut, joyent mzinga chartly. Greplin kiko glogster ideeli, lijit disqus flickr, bitly zoodles. Woopra kaboodle blippy boxbe oooooc chartly disqus, zappos joukuu kiko eduvant dopplr. Ifttt yoono cloudera ebay imvu hojoki wikia, joyent cloudera dogster zapier chumby. Spock chegg kno lijit palantir, twitter reddit doostang.")
        descriptions.add("Sococo blekko meevee glogster ifttt, insala dopplr akismet. mog glogster diigo. Imeem dropio bebo hojoki, zoosk. Wufoo oovoo joukuu jiglu airbnb joyent, nuvvo yuntaa foodzie. Geni glogster ifttt lijit twitter kno geni, mzinga zappos waze odeo zanga. Blippy heekya nuvvo rovio oooj eskobo knewton wikia woopra, palantir yammer weebly sococo ning woopra. Joost heekya xobni yoono disqus, knewton airbnb. Fleck loopt voki disqus palantir zimbra ebay, reddit chartly zoodles etsy. Jabber omgpop fleck oooj zynga, chartly insala. Zoho qeyno omgpop orkut etsy joukuu, koofers plickers oovoo. cotweet grockit yammer. Knewton plugg etsy loopt dogster octopart, imeem chegg yammer. Shopify ngmoco mobly, joukuu. Zimbra convore napster chumby, scribd prezi. Plaxo voki diigo jajah bebo zillow, kippt movity meebo prezi.")
        descriptions.add("Zimbra wikia glogster, joukuu. Ning wikia woopra squidoo, udemy kippt. Grockit wufoo gsnap joost boxbe, unigo blippy oooj. Unigo edmodo kazaa, meevee. Lijit joyent mog babblely revver mzinga, nuvvo imeem elgg. Joyent movity zapier hipmunk wikia, zooomr palantir jajah. Heekya flickr disqus, doostang. Yuntaa joukuu plickers napster chartly convore, doostang mozy jiglu jabber. Mobly groupon chumby joukuu zooomr, omgpop balihoo airbnb.")
        descriptions.add("Greplin tumblr mozy, jaiku. Empressr jumo tivo vimeo chegg, zoosk plugg. rovio twones ideeli. Blippy zoho gsnap imvu kno revver, zynga wakoopa elgg wesabe. Joost doostang meebo xobni kippt, prezi zanga etsy. Stypi groupon yoono plugg, zlio quora. Kazaa balihoo gsnap wikia revver ifttt, babblely jiglu vuvox sclipo. Airbnb mog boxbe ngmoco stypi meebo, lala ideeli joost convore. Jabber sococo oooooc kno kazaa wakoopa chegg, prezi knewton reddit ifttt. Zillow kaboodle meebo squidoo, chegg. Lijit lanyrd gsnap hulu joukuu, lala zooomr. Cotweet blekko jibjab sclipo, oooooc. joukuu reddit napster. Jaiku kaboodle kosmix boxbe elgg joost, elgg dopplr empressr. Blippy etsy gooru appjet scribd napster weebly, voki akismet oooooc odeo.")
        descriptions.add("Zinch udemy greplin fleck empressr zappos spock bebo, orkut balihoo mobly jibjab lala. Revver imeem movity whrrl joost, kiko cotweet wesabe. Loopt xobni cuil divvyshot eduvant groupon scribd, lanyrd edmodo kosmix babblely waze, ebay divvyshot jumo dropio ifttt. zooomr yammer mzinga. Plickers mozy dogster knewton quora, jiglu yoono flickr. Jiglu joyent ning jumo, chegg bitly balihoo, divvyshot prezi. Diigo sifteo nuvvo scribd akismet heekya, twitter eduvant jabber akismet. Sclipo meebo xobni twones blekko zanga, lijit hulu convore. Imvu dopplr zillow plaxo joyent, chegg blippy plaxo kiko palantir, ideeli jajah lijit.")
        descriptions.add("Weebly lala dopplr weebly zynga mog octopart whrrl, chartly voki tivo yammer woopra disqus. Palantir zanga greplin balihoo, omgpop jumo jibjab ngmoco, dropio zimbra. Vuvox wikia wesabe handango revver greplin gooru, yammer chumby unigo voki loopt. Grockit dropio prezi revver loopt mog sclipo, zappos akismet joukuu lala udemy. Tivo dogster zillow chegg, odeo jabber.")
        descriptions.add("Divvyshot sifteo oooj zooomr groupon lala jabber zinch gooru, stypi etsy rovio joukuu blippy oooooc. Weebly bebo cuil ideeli dropio kno voki, omgpop bitly disqus zanga. yuntaa. Zinch zappos airbnb hulu revver twitter, cotweet omgpop chumby doostang octopart, wakoopa loopt squidoo dogster.")
        descriptions.add("Balihoo imeem lijit meevee kno foodzie, sococo lijit chumby zoosk appjet, jabber etsy xobni bubbli. Skype tivo foodzie spock kno, yoono mog kaboodle doostang, kno yuntaa cuil. Joost unigo jibjab xobni omgpop, flickr oovoo rovio. Squidoo etsy gooru elgg, revver plugg.")
        descriptions.add("Palantir jajah vimeo joukuu zimbra, wikia unigo meevee. Knewton disqus ifttt eduvant, meebo eskobo. Grockit knewton xobni rovio revver, eskobo disqus zynga. Oooooc ideeli koofers heroku trulia, groupon weebly fleck jaiku nuvvo, voki joost waze. Skype hulu edmodo gsnap heekya bebo, plaxo mobly hojoki knewton. Ebay chumby hipmunk, ideeli.  Chumby hipmunk ideeli bubbli omgpop, jaiku akismet. Kippt imvu kippt, hulu. Lala babblely napster gooru hulu, handango blekko. Vimeo zoodles skype zanga oovoo, zoodles disqus vimeo, tivo gsnap zooomr. Zinch hojoki divvyshot akismet, ngmoco. Zappos ngmoco eduvant lanyrd zillow, ning shopify.")
       return descriptions.random()

    }
}
