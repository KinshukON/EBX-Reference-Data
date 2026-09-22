# Manufacturing SVG Icon Mapping

This pack keeps the icons as `.svg` files. Where the user-provided flat artwork was applicable, the PNG was embedded inside an SVG wrapper and assigned to the relevant Manufacturing menu/table icons.

| SVG icon | Source artwork | Suggested usage |
|---|---|---|
| `manufacturing.svg` | `factory.png` | Manufacturing / Plant / Factory |
| `plant_master.svg` | `factory.png` | Manufacturing / Plant / Factory |
| `plants_facilities.svg` | `factory.png` | Manufacturing / Plant / Factory |
| `facility.svg` | `factory.png` | Manufacturing / Plant / Factory |
| `factory.svg` | `factory.png` | Standalone source icon: Manufacturing / Plant / Factory |
| `production_line.svg` | `production.png` | Production Line / Work Center / Routing |
| `work_center.svg` | `production.png` | Production Line / Work Center / Routing |
| `routing_master.svg` | `production.png` | Production Line / Work Center / Routing |
| `routing_operation.svg` | `production.png` | Production Line / Work Center / Routing |
| `bill_of_materials_routing.svg` | `production.png` | Production Line / Work Center / Routing |
| `production.svg` | `production.png` | Standalone source icon: Production Line / Work Center / Routing |
| `material_master.svg` | `raw-materials.png` | Raw Materials / Material Master |
| `materials_products.svg` | `raw-materials.png` | Raw Materials / Material Master |
| `inventory_item.svg` | `raw-materials.png` | Raw Materials / Material Master |
| `raw_materials.svg` | `raw-materials.png` | Standalone source icon: Raw Materials / Material Master |
| `commercial_account.svg` | `corporation.png` | Commercial Account / Sales Organization |
| `sales_organization.svg` | `corporation.png` | Commercial Account / Sales Organization |
| `customers_commercial.svg` | `corporation.png` | Commercial Account / Sales Organization |
| `corporation.svg` | `corporation.png` | Standalone source icon: Commercial Account / Sales Organization |
| `customer_master.svg` | `customer-2.png` | Customer Master / Ship-To Bill-To |
| `ship_to_bill_to.svg` | `customer-2.png` | Customer Master / Ship-To Bill-To |
| `customer_2.svg` | `customer-2.png` | Standalone source icon: Customer Master / Ship-To Bill-To |
| `pricing_condition.svg` | `customer-3.png` | Customer Commercial / Pricing / Buying |
| `customer_dashboard.svg` | `customer-3.png` | Customer Commercial / Pricing / Buying |
| `customer_3.svg` | `customer-3.png` | Standalone source icon: Customer Commercial / Pricing / Buying |
| `customer_product_cross_reference.svg` | `customer.png` | Customer Network / Stewardship |
| `stewardship_dashboard.svg` | `customer.png` | Customer Network / Stewardship |
| `customer.svg` | `customer.png` | Standalone source icon: Customer Network / Stewardship |
| `product_master.svg` | `products.png` | Products / SKU / Packaging / Lot Batch |
| `product_variant_sku.svg` | `products.png` | Products / SKU / Packaging / Lot Batch |
| `packaging_material.svg` | `products.png` | Products / SKU / Packaging / Lot Batch |
| `lot_batch_master.svg` | `products.png` | Products / SKU / Packaging / Lot Batch |
| `products.svg` | `products.png` | Standalone source icon: Products / SKU / Packaging / Lot Batch |
| `purchasing_info_record.svg` | `shopping-cart.png` | Purchasing / Procurement |
| `purchasing_organization.svg` | `shopping-cart.png` | Purchasing / Procurement |
| `shopping_cart.svg` | `shopping-cart.png` | Standalone source icon: Purchasing / Procurement |
| `supplier_master.svg` | `supplier-2.png` | Supplier Master / Approved Vendor |
| `suppliers_procurement.svg` | `supplier-2.png` | Supplier Master / Approved Vendor |
| `approved_vendor_list.svg` | `supplier-2.png` | Supplier Master / Approved Vendor |
| `supplier_2.svg` | `supplier-2.png` | Standalone source icon: Supplier Master / Approved Vendor |
| `warehouse_master.svg` | `supplier.png` | Warehouse / Distribution / Storage |
| `distribution_center.svg` | `supplier.png` | Warehouse / Distribution / Storage |
| `storage_location.svg` | `supplier.png` | Warehouse / Distribution / Storage |
| `bin_location.svg` | `supplier.png` | Warehouse / Distribution / Storage |
| `supplier.svg` | `supplier.png` | Standalone source icon: Warehouse / Distribution / Storage |
| `logistics_supply_chain.svg` | `supply-chain.png` | Logistics / Supply Chain / Carrier / Route |
| `supply_chain_dashboard.svg` | `supply-chain.png` | Logistics / Supply Chain / Carrier / Route |
| `carrier_master.svg` | `supply-chain.png` | Logistics / Supply Chain / Carrier / Route |
| `route_master.svg` | `supply-chain.png` | Logistics / Supply Chain / Carrier / Route |
| `shipping_point.svg` | `supply-chain.png` | Logistics / Supply Chain / Carrier / Route |
| `supply_chain.svg` | `supply-chain.png` | Standalone source icon: Logistics / Supply Chain / Carrier / Route |
| `ai_stewardship.svg` | `team.png` | AI Stewardship / Governance / Teams |
| `governance.svg` | `team.png` | AI Stewardship / Governance / Teams |
| `team.svg` | `team.png` | Standalone source icon: AI Stewardship / Governance / Teams |


## v3 Additional User-Provided Manufacturing Icons

The following two attached flat images were added and used as SVG wrappers for the most applicable manufacturing menu items:

| Source image | SVG outputs / aliases | Manufacturing usage |
|---|---|---|
| `factory-2.png` | `factory_2.svg`, `factory_complex.svg`, `manufacturing_industry.svg`, `manufacturing.svg` | Top-level Manufacturing Industry / Manufacturing menu icon; alternate factory icon for accelerator landing areas |
| `industrial-park.png` | `industrial_park.svg`, `production_plant.svg`, `plants_facilities.svg`, `plant_master.svg`, `facility.svg` | Plants & Facilities, Plant Master, Facility, Production Plant and industrial site navigation |

Note: These SVGs preserve the supplied flat artwork by embedding the PNG inside an SVG container with a 512x512 viewBox and 16x16 display dimensions, which keeps them EBX navigation-menu compatible while preserving visual fidelity.
