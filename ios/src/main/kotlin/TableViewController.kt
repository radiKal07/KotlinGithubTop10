import kotlinx.cinterop.*
import main.kotlin.GithubService
import main.kotlin.Repo
import platform.UIKit.*
import platform.Foundation.*
import platform.CoreData.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@ExportObjCClass
class TableViewController(aDecoder: NSCoder) : UITableViewController(aDecoder), UITableViewDataSourceProtocol, UITableViewDelegateProtocol, NSFetchedResultsControllerDelegateProtocol {
    private var data: List<Repo> = ArrayList()
    private var githubService: GithubService =  GithubService()

    override fun viewDidLoad() {
        super.viewDidLoad()

        // get the data
        githubService.getRepos {
            data = it
            dispatch_async(dispatch_get_main_queue(), {
                // refresh the tableview
                tableView.reloadData()
            })
        }
    }

    override fun initWithCoder(aDecoder: NSCoder) = initBy(TableViewController(aDecoder))

    override fun tableView(tableView: UITableView, cellForRowAtIndexPath: NSIndexPath): UITableViewCell {
        val cell = tableView.dequeueReusableCellWithIdentifier("repo", cellForRowAtIndexPath)

        val repo = data[cellForRowAtIndexPath.row.toInt()]
        cell.textLabel?.text = "${repo.title} | Stars: ${repo.stars} | Forks: ${repo.forks}"

        return cell
    }

    override fun tableView(tableView: UITableView, numberOfRowsInSection: Long): Long {
        return data.size.toLong()
    }

    override fun numberOfSectionsInTableView(tableView: UITableView): Long {
        return 1
    }
}