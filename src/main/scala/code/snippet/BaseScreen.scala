package code
package snippet

import net.liftweb._
import http.js._
import http.js.JsCmds._
import http.js.JE._

import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.http.{S, CssBoundLiftScreen}
import net.liftweb.util.{CssSel, BaseField}

import scala.xml.NodeSeq

///*
// * Base all LiftScreens off this. Currently configured to use bootstrap 3.
// */
//abstract class BaseScreen extends Bootstrap3Screen {
//  override def defaultToAjax_? = true
//}

/*
 * Base all LiftScreens off this. Currently configured to use bootstrap.
 */
abstract class BaseScreen extends BootstrapCssBoundLiftScreen {
  override def finishButton =
    <button type="submit" class="next btn btn-primary pull-right" tabindex="1">Save <i class="fa fa-check"></i></button>
}

abstract class BootstrapCssBoundLiftScreen extends CssBoundLiftScreen {
  override protected lazy val cssClassBinding = new BootstrapCssClassBinding

  override def defaultToAjax_? : Boolean = true // this doesn't seem to work when using allTemplate as defined below.

  override def cancelButton = super.cancelButton % ("class" -> "btn btn-default") % ("tabindex" -> "1")
  override def finishButton = super.finishButton % ("class" -> "btn btn-primary") % ("tabindex" -> "1")

  override protected def renderHtml(): NodeSeq = {
    S.appendJs(afterScreenLoad)
    super.renderHtml()
  }

  def displayOnly(fieldName: => String, html: => NodeSeq) =
    new Field {
      type ValueType = String
      override def name = fieldName
      override implicit def manifest = buildIt[String]
      override def default = ""
      override def toForm: Box[NodeSeq] = Full(html)
    }

  protected def afterScreenLoad: JsCmd = JsRaw("""
                                                 |$(".errors").each(function() {
                                                 |  $(this).closest("div.form-group").addClass("has-error");
                                                 |});
                                               """.stripMargin)

  override def allTemplate = savedDefaultXml

  protected def defaultAllTemplate = super.allTemplate

  class BootstrapCssClassBinding extends CssClassBinding {
    override def label = "control-label"
  }

//  /**
//   * Used to change the markup of a field to add an error class.
//   * Additionally, you can hook in your own transformations on individual
//   * screen fields.
//   */
//  def ftrans(stuff: BaseField=>CssSel*): FieldTransform = {
//    FieldTransform((field) => {
//      errchk(field) &
//        stuff.map(_(field)).foldLeft("#notExistent" #> "")(_ & _)
//    })
//  }
//
//  def errchk(field: BaseField): CssSel =
//    if (hasErrors(field)) {
//      ".form-group [class+]" #> "has-error"
//    } else {
//      "#notExistent" #> ""
//    }
//
//  def hasErrors(field: BaseField) = S.errors.exists(t => t._2.isDefined && t._2 == field.uniqueFieldId)

  def fullWidthField(field: BaseField): CssSel = {
    ".top-control-label" #> "" &
      ".top-control-body [class!]" #> "col-sm-9" &
      ".top-control-body [class+]" #> "col-sm-12"
  }

}
